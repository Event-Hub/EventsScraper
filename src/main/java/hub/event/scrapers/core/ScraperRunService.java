package hub.event.scrapers.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@EnableScheduling
class ScraperRunService {
  private final ScraperConfigRepository scraperConfigRepository;
  private final EventCandidateAnalyzer eventCandidateAnalyzer;
  private final EventFacadeAdapter eventFacadeAdapter;
  private final DuplicatedEventCandidateRepository duplicatedEventCandidateRepository;
  private final LastScrapedEventMarkerRepository lastScrapedEventMarkerRepository;
  private final List<PageScraperPort> pageScrapers;

  @Autowired
  ScraperRunService(ScraperConfigRepository scraperConfigRepository, EventCandidateAnalyzer eventCandidateAnalyzer, EventFacadeAdapter eventFacadeAdapter, DuplicatedEventCandidateRepository duplicatedEventCandidateRepository, LastScrapedEventMarkerRepository lastScrapedEventMarkerRepository, List<PageScraperPort> pageScrapers) {
    this.scraperConfigRepository = scraperConfigRepository;
    this.eventCandidateAnalyzer = eventCandidateAnalyzer;
    this.eventFacadeAdapter = eventFacadeAdapter;
    this.duplicatedEventCandidateRepository = duplicatedEventCandidateRepository;
    this.lastScrapedEventMarkerRepository = lastScrapedEventMarkerRepository;
    this.pageScrapers = pageScrapers;
  }

  @Scheduled(cron = "${scrapers.run.cron.expression}")
//  @Scheduled(cron = "0 0 * * *")
  void start() {
    final Collection<ScraperConfig> scraperConfigs = scraperConfigRepository.allScraperConfigs();
    { // zamiast tego kodu można zrobić akcje która się dzieje po tym jak apka wstanie (register new scraper)
      final List<PageScraperPort> scrapersWithoutConfig =
          getScraperThatConfigNotFoundByConfigurationName(scraperConfigs);
      for (PageScraperPort pageScraperPort : scrapersWithoutConfig) {
        // raczej nie mutuj kolekcji, tylko zwróć nowy config którego potem wykorzystasz
        saveActiveConfigAndAppendToConfigList(pageScraperPort, scraperConfigs);
      }
    }

    final List<PageScraperPort> pageScrapersToRun = getActiveScrapersThatShouldBeRun(scraperConfigs);
    final List<ScrapedEvent> scrapedEventList = runScrapersForEvents(pageScrapersToRun);
    final List<AnalyzedEventCandidate> analyzedEventCandidates = eventCandidateAnalyzer.analyze(scrapedEventList);

    final List<ScrapedEvent> notDuplicateScrapedEvents = extractNotDuplicateScrapedEvents(analyzedEventCandidates);
    final List<AnalyzedEventCandidate> analyzedEventCandidateMarkedAsDuplicate = extractDuplicateScrapedEvents(analyzedEventCandidates);

    // do przemyślenia czy w ogóle potrzebne
    final List<DuplicatedEventCandidate> duplicatedEventCandidates = mapAnalyzedEventCandidateToDuplicatedEventCandidate(analyzedEventCandidateMarkedAsDuplicate);

    if (!notDuplicateScrapedEvents.isEmpty()) {
      eventFacadeAdapter.saveAll(notDuplicateScrapedEvents);
    }

    if (!duplicatedEventCandidates.isEmpty()) {
      duplicatedEventCandidateRepository.saveAll(duplicatedEventCandidates);
    }

    final List<String> runScraperConfigurationsNames = getRunScraperConfigurationsNames(pageScrapersToRun);
    lastScrapedEventMarkerRepository.makeDraftActive(runScraperConfigurationsNames);

  }

  private List<PageScraperPort> getScraperThatConfigNotFoundByConfigurationName(Collection<ScraperConfig> scraperConfigs) {
    final List<String> availableScraperConfigByName = scraperConfigs.stream()
        .map(ScraperConfig::configurationName)
        .toList();

    return pageScrapers.stream()
        // filtruj w sqlu
        .filter(pageScraper -> !availableScraperConfigByName.contains(pageScraper.configurationName()))
        .toList();
  }

  private void saveActiveConfigAndAppendToConfigList(PageScraperPort pageScraperPort, Collection<ScraperConfig> scraperConfigs) {
    scraperConfigRepository.create(pageScraperPort.configurationName(), true);
    final ScraperConfig scraperConfig = new ScraperConfig(pageScraperPort.configurationName(), true);

    scraperConfigs.add(scraperConfig);
  }

  private List<PageScraperPort> getActiveScrapersThatShouldBeRun(Collection<ScraperConfig> scraperConfigs) {
    final Map<String, Boolean> configStatusByScraperConfigurationNameMap = scraperConfigs.stream()
        .collect(Collectors.toMap(ScraperConfig::configurationName, ScraperConfig::isActive));

    return pageScrapers.stream()
        .filter(scraper -> configStatusByScraperConfigurationNameMap.get(scraper.configurationName()))
        .toList();
  }

  private List<ScrapedEvent> runScrapersForEvents(List<PageScraperPort> pageScrapersToRun) {
    return pageScrapersToRun.parallelStream()
        .map(PageScraperPort::scrap)
        .flatMap(Collection::stream)
        .toList();
  }

  private List<ScrapedEvent> extractNotDuplicateScrapedEvents(List<AnalyzedEventCandidate> analyzedEventCandidates) {
    return analyzedEventCandidates.stream()
        .filter(AnalyzedEventCandidate::isNotDuplicate)
        .map(AnalyzedEventCandidate::scrapedEvent)
        .toList();
  }

  private List<AnalyzedEventCandidate> extractDuplicateScrapedEvents(List<AnalyzedEventCandidate> analyzedEventCandidates) {
    return analyzedEventCandidates.stream()
        .filter(AnalyzedEventCandidate::isDuplicate)
        .toList();
  }

  private List<DuplicatedEventCandidate> mapAnalyzedEventCandidateToDuplicatedEventCandidate(List<AnalyzedEventCandidate> analyzedEventCandidateMarkedAsDuplicate) {
    return analyzedEventCandidateMarkedAsDuplicate.stream()
        .map(this::mapToDuplicatedEventCandidate)
        .toList();
  }

  private DuplicatedEventCandidate mapToDuplicatedEventCandidate(AnalyzedEventCandidate analyzedEventCandidate) {

    final ScrapedEvent scrapedEvent = analyzedEventCandidate.scrapedEvent();
    final List<UUID> duplicateCandidateUUIDsList = analyzedEventCandidate.duplicateCandidateUUIDsList();
    final List<Integer> duplicateEventIdList = analyzedEventCandidate.duplicateEventIdList();

    return new DuplicatedEventCandidate(scrapedEvent, duplicateCandidateUUIDsList, duplicateEventIdList);
  }

  private List<String> getRunScraperConfigurationsNames(List<PageScraperPort> pageScrapersToRun) {
    return pageScrapersToRun.stream()
        .map(PageScraperPort::configurationName)
        .toList();
  }
}

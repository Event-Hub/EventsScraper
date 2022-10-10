package hub.event.scrapers.core;

import hub.event.scrapers.core.scraper.ScraperConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@EnableScheduling
class ScraperRunService {
  private final ScraperConfigRepository scraperConfigRepository;
  private final EventFacadeAdapter eventFacadeAdapter;
  private final LastScrapedEventMarkerRepository lastScrapedEventMarkerRepository;
  private final List<PageScraperPort> pageScrapers;

  @Autowired
  ScraperRunService(ScraperConfigRepository scraperConfigRepository, EventFacadeAdapter eventFacadeAdapter, LastScrapedEventMarkerRepository lastScrapedEventMarkerRepository, List<PageScraperPort> pageScrapers) {
    this.scraperConfigRepository = scraperConfigRepository;
    this.eventFacadeAdapter = eventFacadeAdapter;
    this.lastScrapedEventMarkerRepository = lastScrapedEventMarkerRepository;
    this.pageScrapers = pageScrapers;
  }

  @Scheduled(cron = "${scrapers.run.cron.expression}")
//  @Scheduled(cron = "0 0 * * *")

  @PostConstruct
  void createScrapersConfigsIfMissing() {
    final Collection<ScraperConfig> scraperConfigs = scraperConfigRepository.allScraperConfigs();
    final List<PageScraperPort> scrapersWithoutConfig = getScraperThatConfigNotFoundByConfigurationName(scraperConfigs);

    for (PageScraperPort pageScraperPort : scrapersWithoutConfig) {
      scraperConfigRepository.create(pageScraperPort.configurationName(), pageScraperPort.timeZone(),true);
    }
  }

  void start() {
    final Collection<ScraperConfig> scraperConfigs = scraperConfigRepository.allScraperConfigs();

    final List<PageScraperPort> pageScrapersToRun = getActiveScrapersThatShouldBeRun(scraperConfigs);
    final List<ScrapedEvent> scrapedEventList = runScrapersForEvents(pageScrapersToRun);

    eventFacadeAdapter.saveAll(scrapedEventList);

    final List<String> runScraperConfigurationsNames = getRunScraperConfigurationsNames(pageScrapersToRun);
    lastScrapedEventMarkerRepository.makeDraftActive(runScraperConfigurationsNames);

  }

  private List<PageScraperPort> getScraperThatConfigNotFoundByConfigurationName(Collection<ScraperConfig> scraperConfigs) {
    final List<String> availableScraperConfigByName = scraperConfigs.stream()
        .map(ScraperConfig::configurationName)
        .toList();

    return pageScrapers.stream()
        .filter(pageScraper -> !availableScraperConfigByName.contains(pageScraper.configurationName()))
        .toList();
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

  private List<String> getRunScraperConfigurationsNames(List<PageScraperPort> pageScrapersToRun) {
    return pageScrapersToRun.stream()
        .map(PageScraperPort::configurationName)
        .toList();
  }
}

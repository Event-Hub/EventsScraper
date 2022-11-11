package hub.event.scrapers.core;

import hub.event.scrapers.core.scraper.ScraperConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@EnableScheduling
class ScraperRunService implements ApplicationRunner {

  private final Logger logger = LoggerFactory.getLogger(ScraperRunService.class);
  private final ScraperConfigRepository scraperConfigRepository;
  private final EventFacadeAdapter eventFacadeAdapter;
  private final LastScrapedEventMarkerRepository lastScrapedEventMarkerRepository;
  private final List<PageScraperPort> pageScrapers;
  private final ScraperIdNameCache scraperIdNameCache;

  @Autowired
  ScraperRunService(ScraperConfigRepository scraperConfigRepository, EventFacadeAdapter eventFacadeAdapter, LastScrapedEventMarkerRepository lastScrapedEventMarkerRepository, List<PageScraperPort> pageScrapers, ScraperIdNameCache scraperIdNameCache) {
    this.scraperConfigRepository = scraperConfigRepository;
    this.eventFacadeAdapter = eventFacadeAdapter;
    this.lastScrapedEventMarkerRepository = lastScrapedEventMarkerRepository;
    this.pageScrapers = pageScrapers;
    this.scraperIdNameCache = scraperIdNameCache;
  }

  @Scheduled(cron = "${scrapers.run.cron.expression}")
//  @Scheduled(cron = "0 0 * * *")
  void start() {
    logger.info("Run scraper schedule");
    final Collection<ScraperConfig> scraperConfigs = scraperConfigRepository.allScraperConfigs();

    final List<PageScraperPort> pageScrapersToRun = getActiveScrapersThatShouldBeRun(scraperConfigs);
    logger.debug("Scrapers to run:");
    pageScrapersToRun.forEach(pageScraperPort -> logger.debug(pageScraperPort.configurationName()));
    final List<ScrapedEvent> scrapedEventList = runScrapersForEvents(pageScrapersToRun);

    logger.info("Scan done by all active scrapers, founded events = {}", scrapedEventList.size());
    logger.debug("Scanned events:");
    scrapedEventList.forEach(scrapedEvent -> logger.debug(scrapedEvent.toString()));
    eventFacadeAdapter.saveAll(scrapedEventList);

    final List<Integer> runScraperConfigurationsIds = getRunScraperConfigurationsIds(pageScrapersToRun);
    lastScrapedEventMarkerRepository.setAllAsCompleteByConfigurationsIds(runScraperConfigurationsIds);
    logger.info("Events saved, markers completed ");
    logger.info("Run scraper schedule done");
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
        .map(this::tryRunScraperTask)
        .flatMap(Collection::stream)
        .toList();
  }

  private Collection<ScrapedEvent> tryRunScraperTask(PageScraperPort pageScraperPort) {
    try {
      return pageScraperPort.scrap();
    } catch (Exception runScraperTaskException) {
      logger.error(String.format("Run scraper %s failed", pageScraperPort.configurationName()), runScraperTaskException);
      return new ArrayList<>();
    }
  }

  private List<Integer> getRunScraperConfigurationsIds(List<PageScraperPort> pageScrapersToRun) {
    return pageScrapersToRun.stream()
        .map(PageScraperPort::configurationName)
        .map(scraperIdNameCache::getIdByScraperName)
        .toList();
  }

  @Override
  public void run(ApplicationArguments args) {
    createScrapersConfigsIfMissingAndFillIdNameCache();
  }

  void createScrapersConfigsIfMissingAndFillIdNameCache() {
    final Collection<ScraperConfig> scraperConfigs = scraperConfigRepository.allScraperConfigs();
    scraperIdNameCache.add(scraperConfigs);

    final List<PageScraperPort> scrapersWithoutConfig = getScraperThatConfigNotFoundByConfigurationName(scraperConfigs);

    for (PageScraperPort pageScraperPort : scrapersWithoutConfig) {
      ScraperConfig scraperConfig = scraperConfigRepository.create(pageScraperPort.configurationName(), pageScraperPort.timeZone(), true);
      scraperIdNameCache.add(scraperConfig);
    }
    logger.info("Scrapers config validated and added to cache");
  }
}

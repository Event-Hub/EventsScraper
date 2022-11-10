package hub.event.scrapers.core.runlog;

class EntityScraperRunStatusLogProperties {
  static final String ID = "logId";
  static final String SCRAPER_CONFIGURATION_NAME_PATH = "scraperConfig.configurationName" ;
  static final String SCRAPER_CONFIGURATION_NAME = "configurationName";
  static final String START_TIME = "startTime";
  static final String FINISH_TIME = "finishTime";
  static final String SCANNED_EVENT_COUNT = "scannedEventCount";
  static final String ERROR_COUNT = "errorCount";

  private EntityScraperRunStatusLogProperties() {
  }
}

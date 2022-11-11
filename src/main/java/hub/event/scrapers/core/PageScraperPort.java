package hub.event.scrapers.core;

import hub.event.scrapers.core.runlog.ScraperRunErrorLog;
import hub.event.scrapers.core.runlog.ScraperRunStatusLog;
import hub.event.scrapers.core.scraper.LastScrapedEventMarker;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Optional;

public abstract class PageScraperPort {
  @Autowired
  private ScraperLogRepository scraperLogRepository;
  @Autowired
  private LastScrapedEventMarkerRepository lastScrapedEventMarkerRepository;

  @Autowired
  private ScraperIdNameCache scraperIdNameCache;

  protected String configurationName() {
    // chyba załatwi sprawę w modularnym monolicie
    return this.getClass().getName();
  }

  protected abstract Collection<ScrapedEvent> scrap();

  protected ZoneId timeZone() {
    return ZoneId.systemDefault();
  }

  protected void logError(Instant time, String errorCode, String description) {
    final ScraperRunErrorLog scraperRunErrorLog = new ScraperRunErrorLog(configurationName(), time, errorCode, description);
    scraperLogRepository.save(scraperRunErrorLog);
  }

  protected void logStatus(Instant startTime, Instant finishTime, Integer scannedEventCount, Integer errorCount) {
    final ScraperRunStatusLog scraperRunStatusLog = new ScraperRunStatusLog(configurationName(), startTime, finishTime, scannedEventCount, errorCount);
    scraperLogRepository.save(scraperRunStatusLog);
  }

  protected Optional<LastScrapedEventMarker> lastScrapedEventMarkerByConfigurationName() {
    Integer id = scraperIdNameCache.getIdByScraperName(configurationName());
    return lastScrapedEventMarkerRepository.findLastCompletedByScraperConfigurationId(id);
  }

  protected void saveLastScrapedEventMarker(Instant runDateTime, String eventTitle, String marker) {
    final LastScrapedEventMarker newScrapedEventMarkerToSave = new LastScrapedEventMarker(configurationName(), runDateTime, eventTitle, marker);
    lastScrapedEventMarkerRepository.store(newScrapedEventMarkerToSave);
  }

}

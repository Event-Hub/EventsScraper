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
  private ScraperRunLogRepository scraperRunLogRepository;
  @Autowired
  private LastScrapedEventMarkerRepository lastScrapedEventMarkerRepository;

  String configurationName() {
    // chyba załatwi sprawę w modularnym monolicie
    return this.getClass().getName();
  }

  protected abstract Collection<ScrapedEvent> scrap();

  ZoneId timeZone() {
    return ZoneId.systemDefault();
  }

  void logError(Instant time, String errorCode, String description) {
    final ScraperRunErrorLog scraperRunErrorLog = new ScraperRunErrorLog(configurationName(), time, errorCode, description);
    scraperRunLogRepository.save(scraperRunErrorLog);
  }

  void logStatus(Instant startTime, Instant finishTime, Integer scannedEventCount, Integer errorCount) {
    final ScraperRunStatusLog scraperRunStatusLog = new ScraperRunStatusLog(configurationName(), startTime, finishTime, scannedEventCount, errorCount);
    scraperRunLogRepository.save(scraperRunStatusLog);
  }

  Optional<LastScrapedEventMarker> lastScrapedEventMarkerByConfigurationName() {
    return lastScrapedEventMarkerRepository.findByScraperConfigurationName(configurationName(), true);
  }

  void saveLastScrapedEventMarker(Instant runDateTime, String eventTitle, String marker) {
    final Optional<LastScrapedEventMarker> savedScrapedEventMarker = lastScrapedEventMarkerRepository.findByScraperConfigurationName(configurationName(), false);
    savedScrapedEventMarker.ifPresent(lastScrapedEventMarkerRepository::drop);

    final LastScrapedEventMarker newScrapedEventMarkerToSave = new LastScrapedEventMarker(configurationName(), runDateTime, eventTitle, marker);
    lastScrapedEventMarkerRepository.store(newScrapedEventMarkerToSave);
  }

}

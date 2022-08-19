package hub.event.scrapers.core.runlog;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ScraperRunLogFacade {
  private final ScraperRunLogRepository scraperRunLogRepository;

  public ScraperRunLogFacade(ScraperRunLogRepository scraperRunLogRepository) {
    this.scraperRunLogRepository = scraperRunLogRepository;
  }


  public void logError(String configurationName, LocalDateTime time, String errorCode, String description) {
  }

  public void logStatus(String configurationName, LocalDateTime startTime, LocalDateTime finishTime, Integer scannedEventCount, Integer errorCount) {
  }
}

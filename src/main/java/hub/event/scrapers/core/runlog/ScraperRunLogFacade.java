package hub.event.scrapers.core.runlog;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ScraperRunLogFacade {
  private final ScraperRunLogRepository scraperRunLogRepository;

  public ScraperRunLogFacade(ScraperRunLogRepository scraperRunLogRepository) {
    this.scraperRunLogRepository = scraperRunLogRepository;
  }


  public void logError(String configurationName, LocalDateTime time, String errorCode, String description) {
    final ScraperRunErrorLog scraperRunErrorLog = new ScraperRunErrorLog(configurationName, time, errorCode, description);
    scraperRunLogRepository.save(scraperRunErrorLog);
  }

  public void logStatus(String configurationName, LocalDateTime startTime, LocalDateTime finishTime, Integer scannedEventCount, Integer errorCount) {
    final ScraperRunStatusLog scraperRunStatusLog = new ScraperRunStatusLog(configurationName, startTime, finishTime, scannedEventCount, errorCount);
    scraperRunLogRepository.save(scraperRunStatusLog);
  }

  public List<ScraperRunErrorLog> findAllErrorLog(ErrorLogSearchCommand errorLogSearchCommand) {
    return scraperRunLogRepository.findAllErrorLog(errorLogSearchCommand);
  }
  public List<ScraperRunStatusLog> findAllStatusLog(StatusLogSearchCommand statusLogSearchCommand) {
    return scraperRunLogRepository.findAllStatusLog(statusLogSearchCommand);
  }


}

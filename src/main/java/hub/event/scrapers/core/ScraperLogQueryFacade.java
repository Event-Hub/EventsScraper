package hub.event.scrapers.core;

import hub.event.scrapers.core.runlog.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScraperLogQueryFacade {
  private final ScraperLogRepository scraperLogRepository;

  public ScraperLogQueryFacade(ScraperLogRepository scraperLogRepository) {
    this.scraperLogRepository = scraperLogRepository;
  }
  public List<ScraperRunErrorLog> findAllErrorLog(ErrorLogSearchQuery errorLogSearchQuery) {
    return scraperLogRepository.findAllErrorLog(errorLogSearchQuery);
  }
  public List<ScraperRunStatusLog> findAllStatusLog(StatusLogSearchQuery statusLogSearchQuery) {
    return scraperLogRepository.findAllStatusLog(statusLogSearchQuery);
  }

}

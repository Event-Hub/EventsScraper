package hub.event.scrapers.core;

import hub.event.scrapers.core.runlog.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScraperQueryFacade {
  private final ScraperRunLogRepository scraperRunLogRepository;

  public ScraperQueryFacade(ScraperRunLogRepository scraperRunLogRepository) {
    this.scraperRunLogRepository = scraperRunLogRepository;
  }
  public List<ScraperRunErrorLog> findAllErrorLog(ErrorLogSearchQuery errorLogSearchQuery) {
    return scraperRunLogRepository.findAllErrorLog(errorLogSearchQuery);
  }
  public List<ScraperRunStatusLog> findAllStatusLog(StatusLogSearchQuery statusLogSearchQuery) {
    return scraperRunLogRepository.findAllStatusLog(statusLogSearchQuery);
  }

}

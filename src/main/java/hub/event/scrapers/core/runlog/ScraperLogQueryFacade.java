package hub.event.scrapers.core.runlog;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScraperLogQueryFacade {
  private final ScraperLogQueryRepository scraperLogRepository;

  public ScraperLogQueryFacade(ScraperLogQueryRepository scraperLogQueryRepository) {
    this.scraperLogRepository = scraperLogQueryRepository;
  }

  public List<ScraperRunErrorLog> findAllErrorLog(ErrorLogSearchQuery errorLogSearchQuery) {
    return scraperLogRepository.findAllErrorLog(errorLogSearchQuery);
  }

  public List<ScraperRunStatusLog> findAllStatusLog(StatusLogSearchQuery statusLogSearchQuery) {
    return scraperLogRepository.findAllStatusLog(statusLogSearchQuery);
  }

}

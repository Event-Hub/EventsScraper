package hub.event.scrapers.core;

import hub.event.scrapers.core.runlog.ScraperRunErrorLog;
import hub.event.scrapers.core.runlog.ScraperRunStatusLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ScraperLogRepository {
  private final Logger logger = LoggerFactory.getLogger(ScraperLogRepository.class);
  private final JpaScraperRunLogRepository jpaScraperRunLogRepository;
  private final JpaScraperRunErrorRepository jpaScraperRunErrorRepository;
  private final ScraperIdNameCache scraperIdNameCache;

  @Autowired
  public ScraperLogRepository(JpaScraperRunLogRepository jpaScraperRunLogRepository, JpaScraperRunErrorRepository jpaScraperRunErrorRepository, ScraperIdNameCache scraperIdNameCache) {
    this.jpaScraperRunLogRepository = jpaScraperRunLogRepository;
    this.jpaScraperRunErrorRepository = jpaScraperRunErrorRepository;
    this.scraperIdNameCache = scraperIdNameCache;
  }

  void save(ScraperRunStatusLog scraperRunStatusLog) {
    EntityScraperRunStatusLog entityScraperRunStatusLog = mapToEntity(scraperRunStatusLog);
    logger.debug("Mapped EntityScraperRunStatusLog :{}", entityScraperRunStatusLog);
    jpaScraperRunLogRepository.save(entityScraperRunStatusLog);
  }

  void save(ScraperRunErrorLog scraperRunError) {
    EntityScraperRunErrorLog entityScraperRunErrorLog = mapToEntity(scraperRunError);
    logger.debug("Mapped EntityScraperRunErrorLog :{}", entityScraperRunErrorLog);
    jpaScraperRunErrorRepository.save(entityScraperRunErrorLog);
  }


  private EntityScraperRunStatusLog mapToEntity(ScraperRunStatusLog scraperRunStatusLog) {
    final Integer scraperId = scraperIdNameCache.getIdByScraperName(scraperRunStatusLog.configurationName());

    return new EntityScraperRunStatusLog(
        scraperId,
        scraperRunStatusLog.startTime(),
        scraperRunStatusLog.finishTime(),
        scraperRunStatusLog.scannedEventCount(),
        scraperRunStatusLog.errorCount()
    );
  }

  private EntityScraperRunErrorLog mapToEntity(ScraperRunErrorLog scraperRunError) {
    final Integer scraperId = scraperIdNameCache.getIdByScraperName(scraperRunError.configurationName());

    return new EntityScraperRunErrorLog(
        scraperId,
        scraperRunError.time(),
        scraperRunError.errorCode(),
        scraperRunError.description()
    );
  }

}

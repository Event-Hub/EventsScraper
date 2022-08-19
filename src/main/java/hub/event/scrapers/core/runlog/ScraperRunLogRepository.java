package hub.event.scrapers.core.runlog;

interface ScraperRunLogRepository {

 void save(ScraperRunStatusLog scraperRunStatusLog);

 void save (ScraperRunErrorLog scraperRunError);
}

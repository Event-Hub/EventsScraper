package hub.event.scrapers.core.runlog;

import java.util.List;

interface ScraperRunLogRepository {

 void save(ScraperRunStatusLog scraperRunStatusLog);

 void save (ScraperRunErrorLog scraperRunError);

 List<ScraperRunErrorLog> findAllErrorLog(ErrorLogSearchCommand errorLogSearchCommand);

 List<ScraperRunStatusLog> findAllStatusLog(StatusLogSearchCommand statusLogSearchCommand);
}

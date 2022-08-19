package hub.event.scrapers.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@EnableScheduling
class ScraperRunService {
  private final ScraperConfigRepository scraperConfigRepository;
  private final List<PageScraperPort> pageScrapers;

  @Autowired
  ScraperRunService(ScraperConfigRepository scraperConfigRepository, List<PageScraperPort> pageScrapers) {
    this.scraperConfigRepository = scraperConfigRepository;
    this.pageScrapers = pageScrapers;
  }

  @Scheduled(cron = "${scrapers.run.cron.expression}")
//  @Scheduled(cron = "0 0 * * *")
  void start() {
  }
}

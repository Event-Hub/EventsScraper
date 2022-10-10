package hub.event.scrapers.core;

import hub.event.scrapers.core.exceptions.ScraperConfigurationByNameNotExists;
import org.springframework.stereotype.Service;

import java.time.ZoneId;

@Service
public class ScraperFacade {

  private final ScraperConfigRepository scraperConfigRepository;

  public ScraperFacade(ScraperConfigRepository scraperConfigRepository) {
    this.scraperConfigRepository = scraperConfigRepository;
  }

  public void activateScraperByConfigurationName(String scraperName) {
    //TODO nightmare throw exception if config not exists
    if (scraperConfigRepository.exists(scraperName)) {
      scraperConfigRepository.activate(scraperName);
    } else {
      scraperConfigRepository.create(scraperName, ZoneId.systemDefault(),true);
    }
  }

  public void deactivateScraperByConfigurationName(String scraperName) throws ScraperConfigurationByNameNotExists {
    if (!scraperConfigRepository.exists(scraperName)) {
      throw new ScraperConfigurationByNameNotExists(scraperName);
    }

    scraperConfigRepository.deactivate(scraperName);
  }

}

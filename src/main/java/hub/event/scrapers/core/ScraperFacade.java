package hub.event.scrapers.core;

import hub.event.scrapers.core.exceptions.ScraperConfigurationByNameNotExists;
import org.springframework.stereotype.Service;

@Service
public class ScraperFacade {

  private final ScraperConfigRepository scraperConfigRepository;
  private final ScraperIdNameCache scraperIdNameCache;

  public ScraperFacade(ScraperConfigRepository scraperConfigRepository, ScraperIdNameCache scraperIdNameCache) {
    this.scraperConfigRepository = scraperConfigRepository;
    this.scraperIdNameCache = scraperIdNameCache;
  }

  public void activateScraperByConfigurationName(String scraperName) throws ScraperConfigurationByNameNotExists {
    Integer scraperId = scraperIdNameCache.getIdByScraperName(scraperName);
    validateScraperExists(scraperName, scraperId);
    scraperConfigRepository.activate(scraperId);
  }

  public void deactivateScraperByConfigurationName(String scraperName) throws ScraperConfigurationByNameNotExists {
    Integer scraperId = scraperIdNameCache.getIdByScraperName(scraperName);
    validateScraperExists(scraperName, scraperId);
    scraperConfigRepository.deactivate(scraperId);
  }

  private void validateScraperExists(String scraperName, Integer scraperId) throws ScraperConfigurationByNameNotExists {
    if (!scraperConfigRepository.exists(scraperId)) {
      throw new ScraperConfigurationByNameNotExists(scraperName);
    }
  }
}

package hub.event.scrapers.core;

import hub.event.scrapers.core.scraper.ScraperConfig;
import org.springframework.stereotype.Repository;

import java.time.ZoneId;
import java.util.List;

@Repository
class ScraperConfigRepository {
  private final JpaScraperConfigRepository jpaScraperConfigRepository;

  ScraperConfigRepository(JpaScraperConfigRepository jpaScraperConfigRepository) {
    this.jpaScraperConfigRepository = jpaScraperConfigRepository;
  }


  boolean exists(Integer scraperId) {
    return jpaScraperConfigRepository.existsById(scraperId);
  }

  ScraperConfig create(String scraperName, ZoneId timeZone, boolean activeState) {
    final EntityScraperConfig entityScraperConfig = new EntityScraperConfig(scraperName, timeZone.toString(), activeState);
    final EntityScraperConfig savedEntity = jpaScraperConfigRepository.save(entityScraperConfig);
    return mapToScraperConfig(savedEntity);
  }

  void activate(Integer scraperId) {
    jpaScraperConfigRepository.setActiveState(scraperId, true);
  }

  void deactivate(Integer scraperId) {
    jpaScraperConfigRepository.setActiveState(scraperId, false);
  }

  public List<ScraperConfig> allScraperConfigs() {
    return jpaScraperConfigRepository.findAll()
        .stream()
        .map(this::mapToScraperConfig)
        .toList();
  }

  private ScraperConfig mapToScraperConfig(EntityScraperConfig entityScraperConfig) {
    ZoneId timeZone = ZoneId.of(entityScraperConfig.getTimeZone());
    return new ScraperConfig(entityScraperConfig.getScraperId(), entityScraperConfig.getConfigurationName(), timeZone, entityScraperConfig.isActive());
  }
}

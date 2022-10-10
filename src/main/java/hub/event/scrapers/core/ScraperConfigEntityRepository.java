package hub.event.scrapers.core;

import hub.event.scrapers.core.scraper.ScraperConfig;
import org.springframework.stereotype.Repository;

import java.time.ZoneId;
import java.util.List;

@Repository
class ScraperConfigEntityRepository implements ScraperConfigRepository {
  private final ScraperConfigJpaRepository scraperConfigJpaRepository;

  ScraperConfigEntityRepository(ScraperConfigJpaRepository scraperConfigJpaRepository) {
    this.scraperConfigJpaRepository = scraperConfigJpaRepository;
  }

  @Override
  public boolean exists(String scraperName) {
    return scraperConfigJpaRepository.existsById(scraperName);
  }

  @Override
  public void create(String scraperName, ZoneId timeZone ,boolean activeState) {
    final ScraperConfigEntity scraperConfigEntity = new ScraperConfigEntity(scraperName, timeZone.toString(), activeState);
    scraperConfigJpaRepository.save(scraperConfigEntity);
  }

  @Override
  public void activate(String scraperConfigurationName) {
    scraperConfigJpaRepository.setActiveState(scraperConfigurationName, true);
  }

  @Override
  public void deactivate(String scraperConfigurationName) {
    scraperConfigJpaRepository.setActiveState(scraperConfigurationName, false);
  }

  @Override
  public List<ScraperConfig> allScraperConfigs() {
    return scraperConfigJpaRepository.findAll()
        .stream()
        .map(this::mapToScraperConfig)
        .toList();
  }

  private ScraperConfig mapToScraperConfig(ScraperConfigEntity scraperConfigEntity) {
    ZoneId timeZone = ZoneId.of(scraperConfigEntity.getTimeZone());
    return new ScraperConfig(scraperConfigEntity.getConfigurationName(), timeZone, scraperConfigEntity.isActive());
  }
}

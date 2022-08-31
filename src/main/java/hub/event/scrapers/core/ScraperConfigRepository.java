package hub.event.scrapers.core;

import java.util.Collection;

interface ScraperConfigRepository {

  boolean exists(String scraperName);

  void create(String scraperName, boolean activeState);

  void activate(String scraperConfigurationName);

  void deactivate(String scraperConfigurationName);

  Collection<ScraperConfig> allScraperConfigs();
}

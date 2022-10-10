package hub.event.scrapers.core;


import hub.event.scrapers.core.scraper.ScraperConfig;

import java.time.ZoneId;
import java.util.List;

public interface ScraperConfigRepository {

  boolean exists(String scraperName);

  void create(String scraperName, ZoneId timeZone, boolean activeState);

  void activate(String scraperConfigurationName);

  void deactivate(String scraperConfigurationName);

  List<ScraperConfig> allScraperConfigs();
}

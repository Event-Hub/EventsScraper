package hub.event.scrapers.core;

import hub.event.scrapers.core.scraper.ScraperConfig;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

@Component
class ScraperIdNameCache {
  private final ConcurrentHashMap<String, Integer> nameToIdMap;
  private final ConcurrentHashMap<Integer, String> idToNameMap;

  ScraperIdNameCache() {
    nameToIdMap = new ConcurrentHashMap<>();
    idToNameMap = new ConcurrentHashMap<>();
  }

  Integer getIdByScraperName(String scraperConfigurationName) {
    return nameToIdMap.get(scraperConfigurationName);
  }

  String getScraperNameById(Integer scraperId) {
    return idToNameMap.get(scraperId);
  }

  void add(Collection<ScraperConfig> scraperConfigs) {
    for (ScraperConfig scraperConfig : scraperConfigs) {
      add(scraperConfig);
    }
  }

  public void add(ScraperConfig scraperConfig) {
    idToNameMap.put(scraperConfig.scraperId(), scraperConfig.configurationName());
    nameToIdMap.put(scraperConfig.configurationName(), scraperConfig.scraperId());
  }
}

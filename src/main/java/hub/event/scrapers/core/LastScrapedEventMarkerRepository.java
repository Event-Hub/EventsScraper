package hub.event.scrapers.core;

import java.util.List;
import java.util.Optional;

public interface LastScrapedEventMarkerRepository {
  void store(LastScrapedEventMarker lastScrapedEventMarker);

  void drop(LastScrapedEventMarker lastScrapedEventMarker);

  void makeDraftActive(List<String> configurationNameList);

  Optional<LastScrapedEventMarker> findByScraperConfigurationName(String configurationName, boolean complete);

}

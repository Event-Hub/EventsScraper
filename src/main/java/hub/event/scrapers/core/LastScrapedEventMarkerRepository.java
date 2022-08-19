package hub.event.scrapers.core;

import java.util.Optional;

public interface LastScrapedEventMarkerRepository {
  void store(LastScrapedEventMarker lastScrapedEventMarker);

  Optional<LastScrapedEventMarker> findByScraperConfigurationName(String configurationName);
}

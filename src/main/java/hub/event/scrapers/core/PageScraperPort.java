package hub.event.scrapers.core;

import java.util.Collection;

public interface PageScraperPort {
  String configurationName();
  Collection<ScrapedEvent> scrap();
}
package hub.event.scrapers.core;

import java.util.Collection;

public interface PageScraperPort {
  default String configurationName() {
    // chyba załatwi sprawę w modularnym monolicie
    return this.getClass().getName();
  }
  Collection<ScrapedEvent> scrap();
}

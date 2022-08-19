package hub.event.scrapers.proanima;

import hub.event.scrapers.core.PageScraperPort;
import hub.event.scrapers.core.ScrapedEvent;

import java.util.Collection;
import java.util.Collections;

class ProanimaScraper implements PageScraperPort {
  @Override
  public String configurationName() {
    return null;
  }

  @Override
  public Collection<ScrapedEvent> scrap() {
    return Collections.emptyList();
  }
}

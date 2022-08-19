package hub.event.scrapers.empikbilet;

import hub.event.scrapers.core.PageScraperPort;
import hub.event.scrapers.core.ScrapedEvent;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

class EmpikBiletScraper implements PageScraperPort {
  @Override
  public String configurationName() {
    return null;
  }

  @Override
  public Collection<ScrapedEvent> scrap() {
    return Collections.emptyList();
  }
}

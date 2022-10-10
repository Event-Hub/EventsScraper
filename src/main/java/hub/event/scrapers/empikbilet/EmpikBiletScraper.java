package hub.event.scrapers.empikbilet;

import hub.event.scrapers.core.PageScraperPort;
import hub.event.scrapers.core.ScrapedEvent;

import java.util.Collection;
import java.util.Collections;

class EmpikBiletScraper extends PageScraperPort {


  @Override
  protected Collection<ScrapedEvent> scrap() {
    return Collections.emptyList();
  }
}

package hub.event.scrapers.empikbilet;

import hub.event.scrapers.core.PageScraperPort;
import hub.event.scrapers.core.ScrapedEvent;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;

@Component
class EmpikBiletScraper extends PageScraperPort {


  @Override
  protected Collection<ScrapedEvent> scrap() {
    return Collections.emptyList();
  }
}

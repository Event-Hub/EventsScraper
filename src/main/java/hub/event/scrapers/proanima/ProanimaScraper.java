package hub.event.scrapers.proanima;

import hub.event.scrapers.core.PageScraperPort;
import hub.event.scrapers.core.ScrapedEvent;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.Collection;

@Component
class ProanimaScraper extends PageScraperPort {
  @Override
  protected Collection<ScrapedEvent> scrap() {
    logError(ZonedDateTime.now().toInstant(), "PRO_ERR_0", "Scraper not implemented yet");
    saveLastScrapedEventMarker(ZonedDateTime.now().toInstant(), null, "Absolutely nothing, i'm just drunk");
    throw new NotImplementedException();
  }
}

package hub.event.scrapers.core;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
class EventFacadeAdapter {
  public void saveAll(List<ScrapedEvent> scrapedEventList) {
    //TODO  integration with EventFacade from event module - mapping to event module API dto
  }
}

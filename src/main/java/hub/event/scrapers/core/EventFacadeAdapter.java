package hub.event.scrapers.core;

import hub.event.events.EventFacade;
import hub.event.events.event.Event;
import hub.event.events.type.Type;
import hub.event.scrapers.core.datewithlocation.MultipleEventDateWithLocations;
import hub.event.scrapers.core.datewithlocation.MultipleMappedEvents;
import hub.event.scrapers.core.datewithlocation.SingleDateMappedToEvent;
import hub.event.scrapers.core.datewithlocation.SingleEventDateWithLocation;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
class EventFacadeAdapter {
  private final EventFacade eventFacade;

  EventFacadeAdapter(EventFacade eventFacade) {
    this.eventFacade = eventFacade;
  }

  public void saveAll(List<ScrapedEvent> scrapedEventList) {
    List<Event> eventList = mapScrapedEventListToEventList(scrapedEventList);
    eventFacade.saveEvents(eventList);
  }

  private List<Event> mapScrapedEventListToEventList(List<ScrapedEvent> scrapedEventList) {
    return scrapedEventList.stream()
        .map(this::mapEvent)
        .flatMap(Collection::stream)
        .toList();
  }

  private List<Event> mapEvent(ScrapedEvent scrapedEvent) {
    return scrapedEvent.hasMultipleDateAndLocations()
        ? mapMultipleDateToEvent(scrapedEvent)
        : mapSingleDateToEvent(scrapedEvent);
  }

  private List<Event> mapMultipleDateToEvent(ScrapedEvent scrapedEvent) {
    final MultipleEventDateWithLocations multipleEventDateWithLocations = scrapedEvent.multipleEventDateWithLocations();

    List<Event> eventList = new MultipleMappedEvents(multipleEventDateWithLocations).events();

    for (Event event : eventList) {
      event.setTitle(scrapedEvent.title());
      event.setDescription(scrapedEvent.description());
      event.setTypes(mapTypes(scrapedEvent.types()));
    }

    return eventList;
  }

  private List<Event> mapSingleDateToEvent(ScrapedEvent scrapedEvent) {
    final SingleEventDateWithLocation eventDateWithLocation = scrapedEvent.singleEventDateWithLocation();
    final Event event = new SingleDateMappedToEvent(eventDateWithLocation);

    event.setTitle(scrapedEvent.title());
    event.setDescription(scrapedEvent.description());
    event.setTypes(mapTypes(scrapedEvent.types()));

    return List.of(event);
  }

  private List<Type> mapTypes(List<String> types) {
    return types.stream()
        .map(type -> new Type(null, type))
        .toList();
  }
}

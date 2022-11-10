package hub.event.scrapers.core.datewithlocation;

import hub.event.events.event.Event;

import java.util.List;
import java.util.stream.Collectors;

public class MultipleMappedEvents {
  private MultipleEventDateWithLocations multipleEventDateWithLocations;

  public MultipleMappedEvents(MultipleEventDateWithLocations multipleEventDateWithLocations) {

    this.multipleEventDateWithLocations = multipleEventDateWithLocations;
  }

  public List<Event> events() {
    return multipleEventDateWithLocations.eventDateWithLocations()
        .stream()
        .map(MultipleDateMappedToEvent::new)
        .collect(Collectors.toList());
  }
}

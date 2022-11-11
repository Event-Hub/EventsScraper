package hub.event.scrapers.core.datewithlocation;

import hub.event.events.city.City;
import hub.event.events.event.Event;
import hub.event.events.place.Place;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

public class MultipleDateMappedToEvent extends Event {
  public MultipleDateMappedToEvent(EventDateWithLocation dateWithLocation) {
    super();

    final LocalDateTime startDateTime = LocalDateTime.of(dateWithLocation.startDate(), dateWithLocation.startTime());
    final ZonedDateTime zonedStartDateTime = ZonedDateTime.of(startDateTime, dateWithLocation.timeZone());

    final City city = new City(null, dateWithLocation.city());
    final Place place = new Place(null, dateWithLocation.address(), null, null);

    setCity(city);
    setPlace(place);
    setStartDate(zonedStartDateTime);
  }
}

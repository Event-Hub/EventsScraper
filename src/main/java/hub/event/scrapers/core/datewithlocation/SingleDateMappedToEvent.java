package hub.event.scrapers.core.datewithlocation;

import hub.event.events.city.City;
import hub.event.events.event.Event;
import hub.event.events.place.Place;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.Optional;

public class SingleDateMappedToEvent extends Event {

  public SingleDateMappedToEvent(SingleEventDateWithLocation dateWithLocation) {
    final LocalDateTime startDateTime = LocalDateTime.of(dateWithLocation.startDate(), dateWithLocation.startTime());
    final ZonedDateTime zonedStartDateTime = ZonedDateTime.of(startDateTime, dateWithLocation.timeZone());


    final City city = new City(null, dateWithLocation.city());
    final Place place = new Place(null, dateWithLocation.address(), null, null);

    setCity(city);
    setPlace(place);
    setStartDate(zonedStartDateTime);

    if (Objects.nonNull(dateWithLocation.endDate())) {
      final LocalDateTime endDateTime = LocalDateTime.of(dateWithLocation.endDate(), Optional.ofNullable(dateWithLocation.endTime()).orElse(LocalTime.of(0, 0)));
      final ZonedDateTime endStartDateTime = ZonedDateTime.of(endDateTime, dateWithLocation.timeZone());
      setEndDate(endStartDateTime);
    }
  }
}

package hub.event.scrapers.core.datewithlocation;

import hub.event.scrapers.core.exceptions.EventDateInPastException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;

public class MultipleEventDateWithLocations {
  private Collection<EventDateWithLocation> eventDateWithLocations;

  private MultipleEventDateWithLocations() {
    eventDateWithLocations = new ArrayList<>();
  }

  private MultipleEventDateWithLocations(LocalDate date, LocalTime time, String city, String address, String locationName) throws EventDateInPastException {
    this();
    final EventDateWithLocation eventDateWithLocation = new EventDateWithLocation(date, time, city, address, locationName);
    this.eventDateWithLocations.add(eventDateWithLocation);
  }

  public static MultipleEventDateWithLocations create(LocalDate date, LocalTime time, String city, String address, String locationName) throws EventDateInPastException {
    return new MultipleEventDateWithLocations(date, time, city, address, locationName);
  }

  public MultipleEventDateWithLocations add(LocalDate date, LocalTime time, String city, String address, String locationName) throws EventDateInPastException {
    final EventDateWithLocation eventDateWithLocation = new EventDateWithLocation(date, time, city, address, locationName);
    this.eventDateWithLocations.add(eventDateWithLocation);
    return this;
  }

  Collection<EventDateWithLocation> eventDateWithLocations() {
    return this.eventDateWithLocations;
  }

}

package hub.event.scrapers.core.datewithlocation;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;

public class MultipleEventDateWithLocations {
  private Collection<EventDateWithLocation> eventDateWithLocations;

  public static MultipleEventDateWithLocations create(LocalDate date, LocalTime time, String city, String address, String locationName) {
    return null;
  }

  public MultipleEventDateWithLocations add(LocalDate date, LocalTime time, String city, String address, String locationName) {
    return this;
  }

  Collection<EventDateWithLocation> eventDateWithLocations(){
    return this.eventDateWithLocations;
  }

}

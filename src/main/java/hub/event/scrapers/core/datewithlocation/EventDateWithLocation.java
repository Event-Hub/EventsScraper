package hub.event.scrapers.core.datewithlocation;

import java.time.LocalDate;
import java.time.LocalTime;

class EventDateWithLocation {
  private LocalDate startDate;
  private LocalDate endDate;
  private LocalTime startTime;
  private LocalTime endTime;

  private EventLocation eventLocation;

  String city() {
    return eventLocation.city();
  }

  LocalDate startDate() {
    return startDate;
  }

  LocalTime startTime() {
    return startTime;
  }

  LocalDate endDate() {
    return endDate;
  }

  LocalTime endTime() {
    return endTime;
  }

  String address() {
    return eventLocation.address();
  }

  String locationName() {
    return eventLocation.name();
  }
}

package hub.event.scrapers.core.datewithlocation;

import hub.event.scrapers.core.exceptions.EventDateInPastException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

class EventDateWithLocation {
  private final LocalDate startDate;
  private final LocalDate endDate;
  private final LocalTime startTime;
  private final LocalTime endTime;

  private final EventLocation eventLocation;

  public EventDateWithLocation(LocalDate startDate, LocalTime startTime,LocalDate endDate, LocalTime endTime, String city, String address, String locationName) {
    this.startDate = startDate;
    this.endDate = endDate;
    this.startTime = startTime;
    this.endTime = endTime;
    this.eventLocation = new EventLocation(city, address, locationName);
  }

  public EventDateWithLocation(LocalDate date, LocalTime time, String city, String address, String locationName) throws EventDateInPastException {
    inputDateValidation(date);
    this.startDate = date;
    this.endDate = null;
    this.startTime = time;
    this.endTime = null;
    this.eventLocation = new EventLocation(city, address, locationName);
  }

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

  private void inputDateValidation(LocalDate date) throws EventDateInPastException {
  if(date.isBefore(LocalDate.now())){
    throw new EventDateInPastException(date);
  }
  }
}

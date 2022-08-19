package hub.event.scrapers.core.datewithlocation;

import java.time.LocalDate;
import java.time.LocalTime;

public class SingleEventDateWithLocation {
  private EventDateType eventDateType;
  private EventDateWithLocation eventDateWithLocation;

  private SingleEventDateWithLocation() {

  }

  public static SingleEventDateWithLocation single(LocalDate startDate, LocalTime startTime, String city, String address, String locationName) {
    return null;
  }

  public static SingleEventDateWithLocation single(LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime, String city, String address, String locationName) {
    return null;
  }

  public static SingleEventDateWithLocation period(LocalDate startDate, LocalDate endDate, LocalTime startTime, String city, String address, String locationName) {
    return null;
  }

  public static SingleEventDateWithLocation period(LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime, String city, String address, String locationName) {
    return null;
  }

  LocalDate startDate() {
    return eventDateWithLocation.startDate();
  }

  LocalTime startTime() {
    return eventDateWithLocation.startTime();
  }

  String city() {
    return eventDateWithLocation.city();
  }

  LocalDate endDate() {
    return eventDateWithLocation.endDate();
  }

  LocalTime endTime() {
    return eventDateWithLocation.endTime();
  }


  boolean isSingleDate() {
    return EventDateType.SINGLE.equals(this.eventDateType);
  }

  boolean isPeriodDate() {
    return EventDateType.PERIOD.equals(this.eventDateType);
  }

  String address() {
    return eventDateWithLocation.address();
  }

  String locationName() {
    return eventDateWithLocation.locationName();
  }

}

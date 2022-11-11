package hub.event.scrapers.core.datewithlocation;

import hub.event.scrapers.core.exceptions.EventDateEndDateTimeBeforeStartDateTimeException;
import hub.event.scrapers.core.exceptions.EventDateInPastException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;

public class SingleEventDateWithLocation {
  private final EventDateType eventDateType;
  private final EventDateWithLocation eventDateWithLocation;

  private SingleEventDateWithLocation(EventDateType eventDateType, LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime, ZoneId timeZone, String city, String address, String locationName) {
    this.eventDateType = eventDateType;
    this.eventDateWithLocation = new EventDateWithLocation(startDate, startTime, endDate, endTime, timeZone, city, address, locationName);
  }

  public static SingleEventDateWithLocation single(LocalDate startDate, LocalTime startTime, ZoneId timeZone, String city, String address, String locationName) throws EventDateInPastException {
    validateStartDateTime(startDate, startTime);
    return new SingleEventDateWithLocation(EventDateType.SINGLE, startDate, startTime, null, null, timeZone, city, address, locationName);
  }

  public static SingleEventDateWithLocation single(LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime, ZoneId timeZone, String city, String address, String locationName) throws EventDateEndDateTimeBeforeStartDateTimeException, EventDateInPastException {
    validateStartDateTimeAndEndDateTime(startDate, startTime, endDate, endTime);
    return new SingleEventDateWithLocation(EventDateType.SINGLE, startDate, startTime, endDate, endTime, timeZone, city, address, locationName);
  }

  public static SingleEventDateWithLocation period(LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime, ZoneId timeZone, String city, String address, String locationName) throws EventDateEndDateTimeBeforeStartDateTimeException, EventDateInPastException {
    validateStartDateTimeAndEndDateTime(startDate, startTime, endDate, endTime);
    return new SingleEventDateWithLocation(EventDateType.PERIOD, startDate, startTime, endDate, endTime, timeZone, city, address, locationName);
  }

  public static SingleEventDateWithLocation period(LocalDate startDate, LocalTime startTime, LocalDate endDate, ZoneId timeZone, String city, String address, String locationName) throws EventDateEndDateTimeBeforeStartDateTimeException, EventDateInPastException {
    validateStartDateTimeAndEndDateTime(startDate, startTime, endDate, startTime);
    return new SingleEventDateWithLocation(EventDateType.PERIOD, startDate, startTime, endDate, null, timeZone, city, address, locationName);
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

  ZoneId timeZone(){
    return eventDateWithLocation.timeZone();
  }

  private static void validateStartDateTimeAndEndDateTime(LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime) throws EventDateEndDateTimeBeforeStartDateTimeException, EventDateInPastException {
    final LocalDateTime startLocalDateTime = LocalDateTime.of(startDate, startTime);
    final LocalDateTime endLocalDateTime = LocalDateTime.of(endDate, endTime);
    final LocalDateTime now = LocalDateTime.now();

    if (startLocalDateTime.isBefore(now)) {
      throw new EventDateInPastException(startLocalDateTime);
    }
    if (endLocalDateTime.isBefore(now)) {
      throw new EventDateInPastException(endLocalDateTime);
    }

    if (endLocalDateTime.isBefore(startLocalDateTime)) {
      throw new EventDateEndDateTimeBeforeStartDateTimeException(startLocalDateTime, endLocalDateTime);
    }
  }

  private static void validateStartDateTime(LocalDate startDate, LocalTime startTime) throws EventDateInPastException {
    final LocalDateTime startLocalDateTime = LocalDateTime.of(startDate, startTime);
    final LocalDateTime now = LocalDateTime.now();

    if (startLocalDateTime.isBefore(now)) {
      throw new EventDateInPastException(startLocalDateTime);
    }

  }
}

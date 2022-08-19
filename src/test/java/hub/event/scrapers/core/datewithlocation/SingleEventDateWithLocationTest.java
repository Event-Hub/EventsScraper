package hub.event.scrapers.core.datewithlocation;

import hub.event.scrapers.core.datewithlocation.SingleEventDateWithLocation;
import hub.event.scrapers.core.exceptions.EventDateEndDateBeforeStartDateException;
import hub.event.scrapers.core.exceptions.EventDateEndTimeBeforeStartTimeException;
import hub.event.scrapers.core.exceptions.EventDateInPastException;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class SingleEventDateWithLocationTest {

  @Test
  void whenBuildSingleTypedDateWithIncorrectInputThenThrows() {
    final LocalDate startDate = LocalDate.of(2022, 7, 12);
    final LocalDate startDateInPast = LocalDate.of(2022, 1, 12);
    final LocalTime startTime = LocalTime.of(14, 20);
    final LocalDate incorrectEndDate = LocalDate.of(2022, 7, 11);
    final LocalDate correctEndDate = LocalDate.of(2022, 7, 12);
    final LocalDate endDateInPast = LocalDate.of(2022, 7, 12);
    final LocalTime incorrectEndTime = LocalTime.of(10, 10);
    final LocalTime correctEndTime = LocalTime.of(20, 10);
    final String city = "Thessia";
    final String address = "Nightmare Street 102/34";
    final String locationName = "Black hole mirror club";

    assertThrows(EventDateEndDateBeforeStartDateException.class, () -> SingleEventDateWithLocation.single(startDate, incorrectEndDate, startTime, correctEndTime, city, address, locationName));
    assertThrows(EventDateEndTimeBeforeStartTimeException.class, () -> SingleEventDateWithLocation.single(startDate, correctEndDate, startTime, incorrectEndTime, city, address, locationName));

    assertThrows(EventDateInPastException.class, () -> SingleEventDateWithLocation.single(startDateInPast, startTime, city, address, locationName));
    assertThrows(EventDateInPastException.class, () -> SingleEventDateWithLocation.single(startDateInPast, correctEndDate, startTime, correctEndTime, city, address, locationName));

    assertThrows(EventDateInPastException.class, () -> SingleEventDateWithLocation.single(startDate, endDateInPast, startTime, correctEndTime, city, address, locationName));
  }

  @Test
  void whenBuildPeriodTypedDateWithIncorrectInputThenThrows() {
    final LocalDate startDate = LocalDate.of(2022, 7, 12);
    final LocalDate startDateInPast = LocalDate.of(2022, 1, 12);
    final LocalTime startTime = LocalTime.of(14, 20);
    final LocalDate incorrectEndDate = LocalDate.of(2022, 7, 11);
    final LocalDate correctEndDate = LocalDate.of(2022, 7, 12);
    final LocalDate endDateInPast = LocalDate.of(2022, 2, 12);
    final LocalTime incorrectEndTime = LocalTime.of(10, 10);
    final LocalTime correctEndTime = LocalTime.of(20, 10);
    final String city = "Thessia";
    final String address = "Nightmare Street 102/34";
    final String locationName = "Black hole mirror club";

    assertThrows(EventDateEndDateBeforeStartDateException.class, () -> SingleEventDateWithLocation.period(startDate, incorrectEndDate, startTime, correctEndTime, city, address, locationName));
    assertThrows(EventDateEndTimeBeforeStartTimeException.class, () -> SingleEventDateWithLocation.period(startDate, correctEndDate, startTime, incorrectEndTime, city, address, locationName));

    assertThrows(EventDateInPastException.class, () -> SingleEventDateWithLocation.period(startDateInPast, correctEndDate, startTime, city, address, locationName));
    assertThrows(EventDateInPastException.class, () -> SingleEventDateWithLocation.period(startDateInPast, correctEndDate, startTime, correctEndTime, city, address, locationName));
    assertThrows(EventDateInPastException.class, () -> SingleEventDateWithLocation.period(startDate, endDateInPast, startTime, correctEndTime, city, address, locationName));
  }

  @Test
  void whenCorrectInputThenSingleTypedEventDateBuildCorrectly() {
    final LocalDate startDate = LocalDate.of(2022, 7, 12);
    final LocalTime startTime = LocalTime.of(10, 20);
    final LocalDate endDate = LocalDate.of(2022, 7, 12);
    final LocalTime endTime = LocalTime.of(20, 10);
    final String city = "Thessia";
    final String address = "Nightmare Street 102/34";
    final String locationName = "Black hole mirror club";

    assertDoesNotThrow(() -> {
      SingleEventDateWithLocation singleDateContainsStartDateAndTime = SingleEventDateWithLocation.single(startDate, startTime, city, address, locationName);
      assertNotNull(singleDateContainsStartDateAndTime);

      assertEquals(startDate, singleDateContainsStartDateAndTime.startDate());
      assertEquals(startTime, singleDateContainsStartDateAndTime.startTime());
      assertEquals(city, singleDateContainsStartDateAndTime.city());
      assertEquals(address, singleDateContainsStartDateAndTime.address());
      assertEquals(locationName, singleDateContainsStartDateAndTime.locationName());
      assertNull(singleDateContainsStartDateAndTime.endDate());
      assertNull(singleDateContainsStartDateAndTime.endTime());
      assertTrue(singleDateContainsStartDateAndTime.isSingleDate());
      assertFalse(singleDateContainsStartDateAndTime.isPeriodDate());

    });

    assertDoesNotThrow(() -> {
      SingleEventDateWithLocation fullEventDate = SingleEventDateWithLocation.single(startDate, endDate, startTime, endTime, city, address, locationName);
      assertNotNull(fullEventDate);

      assertEquals(startDate, fullEventDate.startDate());
      assertEquals(startTime, fullEventDate.startTime());
      assertEquals(city, fullEventDate.city());
      assertEquals(endDate, fullEventDate.endDate());
      assertEquals(endTime, fullEventDate.endTime());
      assertEquals(address, fullEventDate.address());
      assertEquals(locationName, fullEventDate.locationName());
      assertTrue(fullEventDate.isSingleDate());
      assertFalse(fullEventDate.isPeriodDate());
    });


  }

  @Test
  void whenCorrectInputThenPeriodTypedEventDateBuildCorrectly() {
    final LocalDate startDate = LocalDate.of(2022, 7, 12);
    final LocalTime startTime = LocalTime.of(14, 0);
    final LocalDate endDate = LocalDate.of(2022, 7, 17);
    final LocalTime endTime = LocalTime.of(16, 30);
    final String city = "Thessia";
    final String address = "Nightmare Street 102/34";
    final String locationName = "Black hole mirror club";

    assertDoesNotThrow(() -> {
      SingleEventDateWithLocation periodDateContainsStartDateAndTime = SingleEventDateWithLocation.period(startDate, endDate, startTime, city, address, locationName);

      assertNotNull(periodDateContainsStartDateAndTime);

      assertEquals(startDate, periodDateContainsStartDateAndTime.startDate());
      assertEquals(startTime, periodDateContainsStartDateAndTime.startTime());
      assertEquals(city, periodDateContainsStartDateAndTime.city());
      assertEquals(endDate, periodDateContainsStartDateAndTime.endDate());
      assertEquals(address, periodDateContainsStartDateAndTime.address());
      assertEquals(locationName, periodDateContainsStartDateAndTime.locationName());
      assertNull(periodDateContainsStartDateAndTime.endTime());
      assertTrue(periodDateContainsStartDateAndTime.isPeriodDate());
      assertFalse(periodDateContainsStartDateAndTime.isSingleDate());
    });

    assertDoesNotThrow(() -> {
      SingleEventDateWithLocation fullEventDate = SingleEventDateWithLocation.period(startDate, endDate, startTime, endTime, city, address, locationName);

      assertNotNull(fullEventDate);

      assertEquals(startDate, fullEventDate.startDate());
      assertEquals(startTime, fullEventDate.startTime());
      assertEquals(city, fullEventDate.city());
      assertEquals(endDate, fullEventDate.endDate());
      assertEquals(endTime, fullEventDate.endTime());
      assertEquals(address, fullEventDate.address());
      assertEquals(locationName, fullEventDate.locationName());
      assertTrue(fullEventDate.isPeriodDate());
      assertFalse(fullEventDate.isSingleDate());
    });
  }

}
package hub.event.scrapers.core.datewithlocation;

import hub.event.scrapers.core.exceptions.EventDateEndDateTimeBeforeStartDateTimeException;
import hub.event.scrapers.core.exceptions.EventDateInPastException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.*;

class SingleEventDateWithLocationTest {

  @Nested
  class SingleTypedEventDateTest {
    @Test
    void whenBuildWithIncorrectInputThenThrows() {
      final LocalDate startDate = LocalDate.now().plusDays(2);
      final LocalDate startDateInPast = LocalDate.of(2022, 1, 12);
      final LocalTime startTime = LocalTime.of(14, 20);
      final LocalDate incorrectEndDate = LocalDate.now().plusDays(1);
      final LocalDate correctEndDate = LocalDate.now().plusDays(2);
      final LocalDate endDateInPast = LocalDate.of(2022, 7, 12);
      final LocalTime incorrectEndTime = LocalTime.of(10, 10);
      final LocalTime correctEndTime = LocalTime.of(20, 10);
      final String city = "Thessia";
      final String address = "Nightmare Street 102/34";
      final String locationName = "Black hole mirror club";
      final ZoneId timeZone = ZoneId.systemDefault();

      assertThrows(EventDateEndDateTimeBeforeStartDateTimeException.class, () -> SingleEventDateWithLocation.single(startDate, startTime, incorrectEndDate, correctEndTime, timeZone, city, address, locationName));
      assertThrows(EventDateEndDateTimeBeforeStartDateTimeException.class, () -> SingleEventDateWithLocation.single(startDate, startTime, correctEndDate, incorrectEndTime, timeZone, city, address, locationName));

      assertThrows(EventDateInPastException.class, () -> SingleEventDateWithLocation.single(startDateInPast, startTime, timeZone, city, address, locationName));
      assertThrows(EventDateInPastException.class, () -> SingleEventDateWithLocation.single(startDateInPast, startTime, correctEndDate, correctEndTime, timeZone, city, address, locationName));

      assertThrows(EventDateInPastException.class, () -> SingleEventDateWithLocation.single(startDate, startTime, endDateInPast, correctEndTime, timeZone, city, address, locationName));
    }

    @Test
    void whenCorrectInputThenBuildCorrectly() {
      final LocalDate startDate = LocalDate.now().plusDays(2);
      final LocalTime startTime = LocalTime.of(10, 20);
      final LocalDate endDate = LocalDate.now().plusDays(2);
      final LocalTime endTime = LocalTime.of(20, 10);
      final String city = "Thessia";
      final String address = "Nightmare Street 102/34";
      final String locationName = "Black hole mirror club";
      final ZoneId timeZone = ZoneId.systemDefault();

      assertDoesNotThrow(() -> {
        SingleEventDateWithLocation singleDateContainsStartDateAndTime = SingleEventDateWithLocation.single(startDate, startTime, timeZone, city, address, locationName);
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
        SingleEventDateWithLocation fullEventDate = SingleEventDateWithLocation.single(startDate, startTime, endDate, endTime, timeZone, city, address, locationName);
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
  }

  @Nested
  class PeriodTypedDateTest {
    @Test
    void whenBuildWithIncorrectInputThenThrows() {
      final LocalDate startDate = LocalDate.now().plusDays(2);
      final LocalDate startDateInPast = LocalDate.of(2022, 1, 12);
      final LocalTime startTime = LocalTime.of(14, 20);
      final LocalDate incorrectEndDate = LocalDate.now().plusDays(1);
      final LocalDate correctEndDate = LocalDate.now().plusDays(2);
      final LocalDate endDateInPast = LocalDate.of(2022, 2, 12);
      final LocalTime incorrectEndTime = LocalTime.of(10, 10);
      final LocalTime correctEndTime = LocalTime.of(20, 10);
      final String city = "Thessia";
      final String address = "Nightmare Street 102/34";
      final String locationName = "Black hole mirror club";
      final ZoneId timeZone = ZoneId.systemDefault();

      assertThrows(EventDateEndDateTimeBeforeStartDateTimeException.class, () -> SingleEventDateWithLocation.period(startDate, startTime, incorrectEndDate, correctEndTime, timeZone, city, address, locationName));
      assertThrows(EventDateEndDateTimeBeforeStartDateTimeException.class, () -> SingleEventDateWithLocation.period(startDate, startTime, correctEndDate, incorrectEndTime, timeZone, city, address, locationName));

      assertThrows(EventDateInPastException.class, () -> SingleEventDateWithLocation.period(startDateInPast, startTime, correctEndDate, timeZone, city, address, locationName));
      assertThrows(EventDateInPastException.class, () -> SingleEventDateWithLocation.period(startDateInPast, startTime, correctEndDate, correctEndTime, timeZone, city, address, locationName));
      assertThrows(EventDateInPastException.class, () -> SingleEventDateWithLocation.period(startDate, startTime, endDateInPast, correctEndTime, timeZone, city, address, locationName));
    }


    @Test
    void whenCorrectInputThenBuildCorrectly() {
      final LocalDate startDate = LocalDate.now().plusDays(2);
      final LocalTime startTime = LocalTime.of(14, 0);
      final LocalDate endDate = LocalDate.now().plusDays(10);
      final LocalTime endTime = LocalTime.of(16, 30);
      final String city = "Thessia";
      final String address = "Nightmare Street 102/34";
      final String locationName = "Black hole mirror club";
      final ZoneId timeZone = ZoneId.systemDefault();

      assertDoesNotThrow(() -> {
        SingleEventDateWithLocation periodDateContainsStartDateAndTime = SingleEventDateWithLocation.period(startDate, startTime, endDate, timeZone, city, address, locationName);

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
        SingleEventDateWithLocation fullEventDate = SingleEventDateWithLocation.period(startDate, startTime, endDate, endTime, timeZone, city, address, locationName);

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
}
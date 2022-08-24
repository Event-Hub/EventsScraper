package hub.event.scrapers.core.datewithlocation;

import hub.event.scrapers.core.exceptions.EventDateInPastException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.*;

class MultipleEventDateWithLocationsTest {

  @Test
  void whenBuildWithIncorrectInputThenThrows() throws EventDateInPastException {
    final LocalDate date = LocalDate.now().plusDays(10);
    final LocalDate dateInPast = LocalDate.of(2022, 7, 12);
    final LocalTime time = LocalTime.of(14, 20);
    final String city = "Thessia";
    final String address= "Nightmare Street 102/34";
    final String locationName = "Black hole mirror club";
    final MultipleEventDateWithLocations multipleEventDateWithLocations = MultipleEventDateWithLocations.create(date, time, city, address, locationName);

    assertThatExceptionOfType(EventDateInPastException.class)
        .isThrownBy(() -> MultipleEventDateWithLocations.create(dateInPast, time, city, address, locationName));

    assertThatExceptionOfType(EventDateInPastException.class)
        .isThrownBy(() -> multipleEventDateWithLocations.add(dateInPast, time, city, address, locationName));
  }

  @Test
  void whenCorrectInputThenBuildCorrectly() {
    final LocalDate date1 = LocalDate.now().plusDays(2);
    final LocalTime time1 = LocalTime.of(10, 20);
    final String city1 = "Thessia";
    final LocalDate date2 = LocalDate.now().plusDays(4);
    final LocalTime time2 = LocalTime.of(13, 0);
    final String city2 = "Eden Prime";
    final LocalDate date3 = LocalDate.now().plusDays(2).plusDays(8);
    final LocalTime time3 = LocalTime.of(18, 30);
    final String city3 = "Rannoch";
    final String address= "Nightmare Street 102/34";
    final String locationName = "Black hole mirror club";

    Assertions.assertThatNoException().isThrownBy(() -> {
      MultipleEventDateWithLocations multipleDate = MultipleEventDateWithLocations.create(date1, time1, city1, address, locationName)
          .add(date2, time2, city2, address, locationName)
          .add(date3, time3, city3, address, locationName);

      assertThat(multipleDate).isNotNull();

      assertThat(multipleDate.eventDateWithLocations()).isNotNull()
          .hasSize(3)
          .extracting(EventDateWithLocation::startDate, EventDateWithLocation::startTime, EventDateWithLocation::city, EventDateWithLocation::address, EventDateWithLocation::locationName)
          .contains(tuple(date1, time1, city1, address, locationName),
              tuple(date2, time2, city2, address, locationName),
              tuple(date3, time3, city3, address, locationName)
          );
    });

  }

}
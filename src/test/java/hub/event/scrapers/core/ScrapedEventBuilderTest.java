package hub.event.scrapers.core;

import hub.event.scrapers.core.datewithlocation.MultipleEventDateWithLocations;
import hub.event.scrapers.core.datewithlocation.SingleEventDateWithLocation;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ScrapedEventBuilderTest {

  @Test
  void whenUseBuilderThenEventIsBuiltCorrectly() {
    //given
    final String title = "Long party on Normandy";
    final String sourceLink = "citadel://eden.news@human.colonies.gh/evens/123edkfke344";
    final String city = "Eden Prime";
    final String address = "Dark Rose 12";
    final String location = "Blue Bar";
    final String description = "example description";
    final Map<String, String> metadata = Map.of("MetaKey1", "MetaValue1", "MetaKey2", "MetaValue2", "MetaKey3", "MetaValue3");
    final LocalDateTime scrapedTime = LocalDateTime.of(2022, 8, 21, 10, 0);

    final LocalDate startDate = LocalDate.of(2022, 10, 12);
    final LocalTime startTime = LocalTime.of(14, 0);
    final LocalDate endDate = LocalDate.of(2022, 10, 16);
    final LocalTime endTime = LocalTime.of(22, 30);

    final SingleEventDateWithLocation singleEventDateWithLocation = SingleEventDateWithLocation.period(startDate, endDate, startTime, endTime, city, address, location);

    final MultipleEventDateWithLocations multipleEventDateWithLocations = MultipleEventDateWithLocations.create(startDate, startTime, city, address, location);

    //then
    ScrapedEvent scrapedEvent1 = ScrapedEvent.builder()
        .title(title)
        .description(description)
        .sourceLink(sourceLink)
        .metadata("MetaKey1", "MetaValue1")
        .metadata("MetaKey2", "MetaValue2")
        .metadata("MetaKey3", "MetaValue3")
        .date(singleEventDateWithLocation)
        .scrapedTime(scrapedTime)
        .build();

    ScrapedEvent scrapedEvent2 = ScrapedEvent.builder()
        .title(title)
        .description(description)
        .sourceLink(sourceLink)
        .date(multipleEventDateWithLocations)
        .scrapedTime(scrapedTime)
        .build();

    ScrapedEvent scrapedEvent3 = ScrapedEvent.builder()
        .title(title)
        .description(description)
        .sourceLink(sourceLink)
        .date(multipleEventDateWithLocations)
        .date(singleEventDateWithLocation)
        .scrapedTime(scrapedTime)
        .build();

    assertNotNull(scrapedEvent1);
    assertNotNull(scrapedEvent2);
    assertNotNull(scrapedEvent3);

    assertEquals(title, scrapedEvent1.title());
    assertEquals(description, scrapedEvent1.description());
    assertEquals(sourceLink, scrapedEvent1.sourceLink());
    assertEquals(metadata, scrapedEvent1.metadata());
    assertEquals(singleEventDateWithLocation, scrapedEvent1.singleEventDateWithLocation());
    assertNull(scrapedEvent1.multipleEventDateWithLocations());
    assertEquals(scrapedTime, scrapedEvent1.scrapedTime());
    assertFalse(scrapedEvent1.hasMultipleDateAndLocations());

    assertEquals(multipleEventDateWithLocations, scrapedEvent2.multipleEventDateWithLocations());
    assertNull(scrapedEvent2.singleEventDateWithLocation());
    assertFalse(scrapedEvent2.hasMultipleDateAndLocations());

    assertEquals(multipleEventDateWithLocations, scrapedEvent3.multipleEventDateWithLocations());
    assertNull(scrapedEvent3.singleEventDateWithLocation());
    assertFalse(scrapedEvent3.hasMultipleDateAndLocations());
  }

}
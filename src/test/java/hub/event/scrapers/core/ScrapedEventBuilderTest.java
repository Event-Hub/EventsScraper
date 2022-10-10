package hub.event.scrapers.core;

import hub.event.scrapers.core.datewithlocation.MultipleEventDateWithLocations;
import hub.event.scrapers.core.datewithlocation.SingleEventDateWithLocation;
import hub.event.scrapers.core.exceptions.EventDateEndDateTimeBeforeStartDateTimeException;
import hub.event.scrapers.core.exceptions.EventDateInPastException;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ScrapedEventBuilderTest {

  @Test
  void whenUseBuilderThenEventIsBuiltCorrectly() throws EventDateInPastException, EventDateEndDateTimeBeforeStartDateTimeException {
    //given
    final String title = "Long party on Normandy";
    final String sourceLink = "citadel://eden.news@human.colonies.gh/evens/123edkfke344";
    final String city = "Eden Prime";
    final String address = "Dark Rose 12";
    final String location = "Blue Bar";
    final String description = "example description";
    final Map<String, String> metadata = Map.of("MetaKey1", "MetaValue1", "MetaKey2", "MetaValue2", "MetaKey3", "MetaValue3");

    final LocalDate startDate = LocalDate.now().plusDays(2);
    final LocalTime startTime = LocalTime.of(14, 0);
    final LocalDate endDate = LocalDate.now().plusDays(4);
    final LocalTime endTime = LocalTime.of(22, 30);
    final ZoneId timeZone = ZoneId.systemDefault();

    final SingleEventDateWithLocation singleEventDateWithLocation = SingleEventDateWithLocation.period(startDate, startTime, endDate, endTime, timeZone, city, address, location);

    final MultipleEventDateWithLocations multipleEventDateWithLocations = MultipleEventDateWithLocations.create(startDate, startTime, timeZone, city, address, location);

    //then
    ScrapedEvent scrapedEvent1 = ScrapedEvent.builder(singleEventDateWithLocation)
        .title(title)
        .description(description)
        .sourceLink(sourceLink)
        .metadata("MetaKey1", "MetaValue1")
        .metadata("MetaKey2", "MetaValue2")
        .metadata("MetaKey3", "MetaValue3")
        .type("Inline Skating")
        .type("Skating Workshops")
        .build();

    ScrapedEvent scrapedEvent2 = ScrapedEvent.builder(multipleEventDateWithLocations)
        .title(title)
        .description(description)
        .sourceLink(sourceLink)
        .build();

    assertNotNull(scrapedEvent1);
    assertNotNull(scrapedEvent2);

    assertEquals(title, scrapedEvent1.title());
    assertEquals(description, scrapedEvent1.description());
    assertEquals(sourceLink, scrapedEvent1.sourceLink());
    assertEquals(metadata, scrapedEvent1.metadata());
    assertEquals(2, scrapedEvent1.types().size());
    assertTrue(scrapedEvent1.types().contains("Inline Skating"));
    assertTrue(scrapedEvent1.types().contains("Skating Workshops"));
    assertEquals(singleEventDateWithLocation, scrapedEvent1.singleEventDateWithLocation());
    assertNull(scrapedEvent1.multipleEventDateWithLocations());
    assertFalse(scrapedEvent1.hasMultipleDateAndLocations());

    assertEquals(multipleEventDateWithLocations, scrapedEvent2.multipleEventDateWithLocations());
    assertNull(scrapedEvent2.singleEventDateWithLocation());
    assertTrue(scrapedEvent2.hasMultipleDateAndLocations());

  }

}
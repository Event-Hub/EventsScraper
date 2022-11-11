package hub.event.scrapers.core;

import hub.event.events.EventFacade;
import hub.event.events.event.Event;
import hub.event.events.type.Type;
import hub.event.scrapers.core.datewithlocation.MultipleEventDateWithLocations;
import hub.event.scrapers.core.datewithlocation.SingleEventDateWithLocation;
import hub.event.scrapers.core.exceptions.EventDateEndDateTimeBeforeStartDateTimeException;
import hub.event.scrapers.core.exceptions.EventDateInPastException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.*;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EventFacadeAdapterTest {

  @Mock
  private EventFacade eventFacade;
  @InjectMocks
  private EventFacadeAdapter eventFacadeAdapter;

  @Captor
  ArgumentCaptor<Iterable<Event>> eventListCaptor;

  @Test
  void saveAllTest() throws EventDateInPastException, EventDateEndDateTimeBeforeStartDateTimeException {
    //given
    final LocalDate date1 = LocalDate.now().plusDays(2);
    final LocalTime time1 = LocalTime.of(10, 20);
    final String city1 = "Thessia";
    final LocalDate date2 = LocalDate.now().plusDays(4);
    final LocalTime time2 = LocalTime.of(13, 0);
    final String city2 = "Eden Prime";
    final LocalDate date3 = LocalDate.now().plusDays(2).plusDays(8);
    final LocalTime time3 = LocalTime.of(18, 30);
    final String city3 = "Rannoch";
    final String address = "Nightmare Street 102/34";
    final String locationName = "Black hole mirror club";
    final ZoneId timeZone = ZoneId.systemDefault();

    final LocalDate startDate = LocalDate.now().plusDays(2);
    final LocalDate endDate = LocalDate.now().plusDays(2);
    final LocalTime startTime = LocalTime.of(10, 20);
    final LocalTime endTime = LocalTime.of(20, 40);
    final String city = "Thessia";
    final String address1 = "Nightmare Street 102/34";
    final String locationName1 = "Black hole mirror club";
    final ZoneId timeZone1 = ZoneId.systemDefault();

    final MultipleEventDateWithLocations multipleDate = MultipleEventDateWithLocations.create(date1, time1, timeZone, city1, address, locationName)
        .add(date2, time2, timeZone, city2, address, locationName)
        .add(date3, time3, timeZone, city3, address, locationName);
    final MultipleEventDateWithLocations multipleDate2 = MultipleEventDateWithLocations.create(date2, time3, timeZone, city1, address, locationName);
    final SingleEventDateWithLocation singleDate1 = SingleEventDateWithLocation.single(startDate, startTime, timeZone1, city, address1, locationName1);
    final SingleEventDateWithLocation singleDate2 = SingleEventDateWithLocation.single(startDate, startTime, endDate, endTime, timeZone1, city, address1, locationName1);

    final ScrapedEvent scrapedEvent1 = ScrapedEvent.builder(multipleDate)
        .title("title1")
        .description("description1")
        .type("type1")
        .type("type2")
        .type("type3")
        .sourceLink("http://eventhub.com/event1")
        .build();

    final ScrapedEvent scrapedEvent2 = ScrapedEvent.builder(multipleDate2)
        .title("title2")
        .description("description2")
        .type("type56")
        .sourceLink("http://eventhub.com/event2")
        .build();

    final ScrapedEvent scrapedEvent3 = ScrapedEvent.builder(singleDate1)
        .title("title3")
        .description("description3")
        .type("type56")
        .sourceLink("http://eventhub.com/event3")
        .build();

    final ScrapedEvent scrapedEvent4 = ScrapedEvent.builder(singleDate2)
        .title("title4")
        .description("description4")
        .type("type564")
        .sourceLink("http://eventhub.com/event4")
        .build();

    final List<ScrapedEvent> scrapedEventList = List.of(scrapedEvent1, scrapedEvent2, scrapedEvent3, scrapedEvent4);
    //then
    eventFacadeAdapter.saveAll(scrapedEventList);

    //verify
    verify(eventFacade).saveEvents(eventListCaptor.capture());

    Iterable<Event> facadeInputEventIterableList = eventListCaptor.getValue();

    assertThat(facadeInputEventIterableList)
        .extracting(
            Event::getTitle,
            Event::getDescription,
            event -> event.getCity().getName(),
            event -> event.getPlace().getName(),
            Event::getStartDate,
            Event::getEndDate,
            event -> event.getTypes().stream().map(Type::getType).sorted().collect(Collectors.joining(","))
        )
        .contains(
            tuple("title1", "description1", city1, address1, ZonedDateTime.of(LocalDateTime.of(date1, time1), timeZone), null, "type1,type2,type3"),
            tuple("title1", "description1", city2, address, ZonedDateTime.of(LocalDateTime.of(date2, time2), timeZone), null, "type1,type2,type3"),
            tuple("title1", "description1", city3, address, ZonedDateTime.of(LocalDateTime.of(date3, time3), timeZone), null, "type1,type2,type3"),
            tuple("title2", "description2", city1, address, ZonedDateTime.of(LocalDateTime.of(date2, time3), timeZone), null, "type56"),
            tuple("title3", "description3", city1, address, ZonedDateTime.of(LocalDateTime.of(startDate, startTime), timeZone), null, "type56"),
            tuple("title4", "description4", city1, address, ZonedDateTime.of(LocalDateTime.of(startDate, startTime), timeZone), ZonedDateTime.of(LocalDateTime.of(endDate, endTime), timeZone), "type564")
        );

  }
}
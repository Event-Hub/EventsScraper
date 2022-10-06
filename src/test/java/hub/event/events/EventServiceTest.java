package hub.event.events;

import hub.event.events.city.City;
import hub.event.events.city.CityRepository;
import hub.event.events.event.Event;
import hub.event.events.event.EventRepository;
import hub.event.events.event.EventService;
import hub.event.events.place.Place;
import hub.event.events.place.PlaceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class EventServiceTest {

    @Autowired
    private EventService eventService;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private CityRepository cityRepository;
    @Autowired PlaceRepository placeRepository;

    @BeforeEach
    void setUp(){
        eventService = new EventService(eventRepository);
    }

    @Test
    void getEventByIdTest(){

        //given
        City city = new City(1L, "city");
        cityRepository.save(city);
        Place place = new Place(1L, "place", "street", 1);
        placeRepository.save(place);
        Event givenEvent = new Event(1L, city, place,
                LocalDate.of(2022,2,9), "title", "desc");
        eventRepository.save(givenEvent);

        //when
        Optional<Event> foundEvent = eventService.getEventById(givenEvent.getId());

        //then
        assertEquals(givenEvent, foundEvent);
     }

     @Test
    void getEventsWithStartDateTest(){

        //given
         City city = new City(1L, "city");
         cityRepository.save(city);
         Place place = new Place(1L, "place", "street", 1);
         placeRepository.save(place);
         Event givenEvent = new Event(1L, city, place,
                 LocalDate.of(2022,2,9), "title", "desc");
         eventRepository.save(givenEvent);
         Event givenEvent2 = new Event(2L, city, place,
                 LocalDate.of(2022,3,12), "title", "desc");
         eventRepository.save(givenEvent2);
         Event givenEvent3 = new Event(3L, city, place,
                 LocalDate.of(2022,2,9), "title3", "desc");
         eventRepository.save(givenEvent3);

         //when
         List<Event> foundEvents = eventService.getEventsWithStartDate(LocalDate.of(2022,2,9));

         //then
         assertEquals(2, foundEvents.size());
     }

}

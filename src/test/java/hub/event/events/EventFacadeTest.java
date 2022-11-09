package hub.event.events;

import hub.event.events.city.City;
import hub.event.events.city.CityRepository;
import hub.event.events.event.Event;
import hub.event.events.event.EventRepository;
import hub.event.events.event.dto.EventDTO;
import hub.event.events.place.Place;
import hub.event.events.place.PlaceRepository;
import hub.event.events.type.Type;
import hub.event.events.type.TypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class EventFacadeTest {

    @Autowired
    private EventFacade eventFacade;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private CityRepository cityRepository;
    @Autowired
    private PlaceRepository placeRepository;
    @Autowired
    private TypeRepository typeRepository;

    @BeforeEach
    void setUp(){
        eventFacade = new EventFacade(eventRepository, cityRepository, placeRepository, typeRepository);
    }

    @Test
    void getEventByIdTest(){

        //given
        City city = new City(1L, "city");
        cityRepository.save(city);
        Place place = new Place(1L, "place", "street", 4);
        placeRepository.save(place);
        Event givenEvent = new Event(1L, city, place,
                ZonedDateTime.of(LocalDateTime.of(2022,3,2,1,0,0),ZoneId.systemDefault()),
                ZonedDateTime.of(LocalDateTime.of(2022,3,3,1,0,0),ZoneId.systemDefault()),
                "title", "desc");
        Type type = new Type(1L, "type");
      //  typeRepository.save(type);
        List<Type> types = new ArrayList<>();
        types.add(type);
        givenEvent.setTypes(types);
        eventRepository.save(givenEvent);

        //when
        EventDTO foundEvent = eventFacade.getEventById(givenEvent.getId()).get();

        //then
        assertEquals(givenEvent, foundEvent);
     }
    @Test
    void getAllEvents(){
        //given
        City city = new City(1L, "city");
        cityRepository.save(city);
        Place place = new Place(1L, "place", "street", 1);
        placeRepository.save(place);
        Event givenEvent = new Event(1L, city, place,
                ZonedDateTime.of(LocalDateTime.of(2022,2,2,1,0,0),ZoneId.systemDefault()),
                ZonedDateTime.of(LocalDateTime.of(2022,3,5,1,0,0),ZoneId.systemDefault()),
                "title", "desc");
        eventRepository.save(givenEvent);
        Event givenEvent2 = new Event(2L, city, place,
                ZonedDateTime.of(LocalDateTime.of(2022,3,2,1,0,0),ZoneId.systemDefault()),
                ZonedDateTime.of(LocalDateTime.of(2022,4,2,1,0,0),ZoneId.systemDefault()),
                "title", "desc");
        eventRepository.save(givenEvent2);
        Event givenEvent3 = new Event(3L, city, place,
                ZonedDateTime.of(LocalDateTime.of(2022,3,2,1,0,0),ZoneId.systemDefault()),
                ZonedDateTime.of(LocalDateTime.of(2022,4,2,1,0,0),ZoneId.systemDefault()),
                "title3", "desc");
        eventRepository.save(givenEvent3);
        //when
        PageRequest pageRequest = PageRequest.of(0,10);
        Page<EventDTO> result = eventFacade.getAllEvents(pageRequest);
        //then
        assertEquals(3,result.getTotalElements());
        assertEquals(1,result.getTotalPages());
    }

    @Test
    void getEventsWithStartDateTest(){

        //given
         City city = new City(1L, "city");
         cityRepository.save(city);
         Place place = new Place(1L, "place", "street", 1);
         placeRepository.save(place);
         Event givenEvent = new Event(1L, city, place,
                 ZonedDateTime.of(LocalDateTime.of(2022,2,2,1,0,0),ZoneId.systemDefault()),
                 ZonedDateTime.of(LocalDateTime.of(2022,3,5,1,0,0),ZoneId.systemDefault()),
                 "title", "desc");
         eventRepository.save(givenEvent);
         Event givenEvent2 = new Event(2L, city, place,
                 ZonedDateTime.of(LocalDateTime.of(2022,3,2,1,0,0),ZoneId.systemDefault()),
                 ZonedDateTime.of(LocalDateTime.of(2022,4,2,1,0,0),ZoneId.systemDefault()),
                 "title", "desc");
         eventRepository.save(givenEvent2);
         Event givenEvent3 = new Event(3L, city, place,
                 ZonedDateTime.of(LocalDateTime.of(2022,3,2,1,0,0),ZoneId.systemDefault()),
                 ZonedDateTime.of(LocalDateTime.of(2022,4,2,1,0,0),ZoneId.systemDefault()),
                 "title3", "desc");
         eventRepository.save(givenEvent3);

         //when
         List<EventDTO> foundEvents = eventFacade
                 .getEventsWithStartDate(ZonedDateTime.of(LocalDateTime.of(2022,3,2,1,0,0),ZoneId.systemDefault()));
         //then
         assertEquals(2, foundEvents.size());
     }

    @Test
    void deleteEvent(){
        //given
        City city = new City(1L, "city");
        cityRepository.save(city);
        Place place = new Place(1L, "place", "street", 1);
        placeRepository.save(place);
        Event givenEvent = new Event(1L, city, place,
                ZonedDateTime.of(LocalDateTime.of(2022,2,2,1,0,0),ZoneId.systemDefault()),
                ZonedDateTime.of(LocalDateTime.of(2022,3,5,1,0,0),ZoneId.systemDefault()),
                "title", "desc");
        eventRepository.save(givenEvent);
        Event givenEvent2 = new Event(2L, city, place,
                ZonedDateTime.of(LocalDateTime.of(2022,3,2,1,0,0),ZoneId.systemDefault()),
                ZonedDateTime.of(LocalDateTime.of(2022,4,2,1,0,0),ZoneId.systemDefault()),
                "title", "desc");
        eventRepository.save(givenEvent2);
        //when
        eventFacade.deleteEvent(givenEvent.getId());
        //then
        assertEquals(1,eventRepository.findAll().size());
    }

}

package hub.event.events;

import hub.event.events.city.CityRepository;
import hub.event.events.event.Event;
import hub.event.events.event.EventMapper;
import hub.event.events.event.EventRepository;
import hub.event.events.event.dto.EventDTO;
import hub.event.events.event.dto.EventUpdateDTO;
import hub.event.events.place.PlaceRepository;
import hub.event.events.type.TypeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

// to jest fasada twojego modułu - API zewnętrzne (modułu), nie API HTTP (bo ono jest już
// drugorzędne, dopasowane pod klienta)
// API modułu nie może eksponować encji bazodanowych,
// jako że utrwalenie to jest drugorzędny szczegół implementacyjny
//
// proponuję zastosować architekturę hexagonalną (clean architecture, onion, ports & adapters)
// opcjalnie można, bo jest tu miejsce, na zastosowanie DDD
@Service
public class EventFacade {

    private final EventRepository eventRepository;
    private final CityRepository cityRepository;
    private final PlaceRepository placeRepository;
    private final TypeRepository typeRepository;
    private final EventMapper eventMapper;

    public EventFacade(EventRepository eventRepository, CityRepository cityRepository, PlaceRepository placeRepository, TypeRepository typeRepository) {
        this.eventRepository = eventRepository;
        this.cityRepository = cityRepository;
        this.placeRepository = placeRepository;
        this.typeRepository = typeRepository;
        this.eventMapper = new EventMapper();
    }

    // to się może przydać do jakiegoś widoku webowego (HTTP API)
    public Page<EventDTO> getAllEvents(Pageable pageable){
        return eventRepository.findAll(pageable).map(eventMapper::map);
    }

    // prawdopodobnie nie użyjemy wcale
    public Optional<EventDTO> getEventById(Long id){
        return eventRepository.findById(id).map(eventMapper::map);
    }

    // to być może użyjemy w newsletterze
    public List<EventDTO> getEventsWithStartDate(ZonedDateTime date){
        return eventRepository.findAllByStartDate(date).stream().map(eventMapper::map).collect(Collectors.toList());
    }

    // strzałka z góry - od Scraperów
    public void saveEvents(Iterable<Event> events) {
        eventRepository.saveAll(events);
    }

    // moderacja
    public void updateEvent(Long id, EventUpdateDTO updateDTO){
        Optional<Event> eventById = eventRepository.findById(id);
        Event event = eventById.orElse(new Event());
        event.setCity(cityRepository.findById(updateDTO.cityId()).get());
        event.setPlace(placeRepository.findById(updateDTO.placeId()).get());
        event.setStartDate(updateDTO.startDate());
        event.setEndDate(updateDTO.endDate());
        event.setTitle(updateDTO.title());
        event.setDescription(updateDTO.description());
        eventRepository.save(event);
    }

    // moderacja wydarzeń
    public void deleteEvent(Long id){
        eventRepository.deleteById(id);
    }
}

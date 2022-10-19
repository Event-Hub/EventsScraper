package hub.event.events.event;

import hub.event.events.city.CityRepository;
import hub.event.events.event.dto.EventUpdateDTO;
import hub.event.events.place.PlaceRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

// to jest fasada twojego modułu - API zewnętrzne (modułu), nie API HTTP (bo ono jest już
// drugorzędne, dopasowane pod klienta)
// API modułu nie może eksponować encji bazodanowych,
// jako że utrwalenie to jest drugorzędny szczegół implementacyjny
//
// proponuję zastosować architekturę hexagonalną (clean architecture, onion, ports & adapters)
// opcjalnie można, bo jest tu miejsce, na zastosowanie DDD
@Service
public class EventService {

    private final EventRepository eventRepository;
    private final CityRepository cityRepository;
    private final PlaceRepository placeRepository;

    public EventService(EventRepository eventRepository, CityRepository cityRepository, PlaceRepository placeRepository) {
        this.eventRepository = eventRepository;
        this.cityRepository = cityRepository;
        this.placeRepository = placeRepository;
    }

    // to się może przydać do jakiegoś widoku webowego (HTTP API)
    public Page<Event> getAllEvents(Pageable pageable){
        return eventRepository.findAll(pageable);
    }

    // prawdopodobnie nie użyjemy wcale
    public Optional<Event> getEventById(Long id){
        return eventRepository.findById(id);
    }

    // to być może użyjemy w newsletterze
    public List<Event> getEventsWithStartDate(ZonedDateTime date){
        return eventRepository.findAllByStartDate(date);
    }

    // strzałka z góry - od Scraperów
    public void saveEvents(Iterable<Event> events) {
        eventRepository.saveAll(events);
    }//TODO test

    // moderacja
    public void updateEvent(Long id, EventUpdateDTO updateDTO){
        Optional<Event> eventById = eventRepository.findById(id);
        Event event = eventById.orElse(new Event());
        event.setCity(cityRepository.findById(updateDTO.city_id()).get());
        event.setPlace(placeRepository.findById(updateDTO.place_id()).get());
        event.setStartDate(updateDTO.startDate());
        event.setEndDate(updateDTO.endDate());
        event.setTitle(updateDTO.title());
        event.setDescription(updateDTO.description());
        eventRepository.save(event);
    } //TODO test

    // moderacja wydarzeń
    public void deleteEvent(Long id){
        eventRepository.deleteById(id);
    }
}

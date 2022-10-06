package hub.event.events.event;

import hub.event.events.event.dto.EventUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    // to się może przydać do jakiegoś widoku webowego (HTTP API)
    public Page<Event> getAllEvents(Pageable pageable){
        return eventRepository.findAll(pageable);
    }//TODO test

    // prawdopodobnie nie użyjemy wcale
    public Optional<Event> getEventById(Long id){
        return eventRepository.findById(id);
    }

    // to być może użyjemy w newsletterze
    public List<Event> getEventsWithStartDate(LocalDate date){
        return eventRepository.findAllByEventDate(date);
    }

    // strzałka z góry - od Scraperów
//    public void saveEvents(Iterable<EventDto> events) {

//    }

    // moderacja
    public void updateEvent(Long id, EventUpdateDTO updateDTO){
        Optional<Event> eventById = eventRepository.findById(id);
        Event event = eventById.orElse(new Event());
        event.setCity(updateDTO.city());
        event.setPlace(updateDTO.place());
        event.setDate(updateDTO.date());
        event.setTitle(updateDTO.title());
        event.setDescription(updateDTO.description());
        eventRepository.save(event);
    } //TODO test

    // moderacja wydarzeń
    public void deleteEvent(Long id){
        eventRepository.deleteById(id);
    }//TODO test
}

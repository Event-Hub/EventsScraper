package hub.event.events.event;

import hub.event.events.event.dto.EventUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class EventService {

    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public Page<Event> getAllEvents(Pageable pageable){
        return eventRepository.findAll(pageable);
    }//TODO test

    public Optional<Event> getEventById(Long id){
        return eventRepository.findById(id);
    }

    public List<Event> getEventsWithStartDate(LocalDate date){
        return eventRepository.findAllByEventDate(date);
    }

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

    public void deleteEvent(Long id){
        eventRepository.deleteById(id);
    }//TODO test
}

package hub.event.events.event;

import hub.event.events.event.dto.EventDTO;

public class EventMapper {

    public EventMapper() {
    }

    public EventDTO map(Event event){
        EventDTO eventDTO = new EventDTO(
                event.getId(),
                event.getCity().getId(),
                event.getPlace().getId(),
                event.getStartDate(),
                event.getEndDate(),
                event.getTitle(),
                event.getDescription());
        return eventDTO;
    }
}

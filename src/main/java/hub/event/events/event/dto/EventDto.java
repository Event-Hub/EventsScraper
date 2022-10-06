package hub.event.events.event.dto;

import hub.event.events.event.vo.Description;
import hub.event.events.event.vo.Name;

public record EventDto(
    Name name,
    Description description
) {
}

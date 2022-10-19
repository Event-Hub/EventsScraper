package hub.event.events.event.dto;

import java.time.ZonedDateTime;

public record EventUpdateDTO(
        Long city_id,
        Long place_id,
        ZonedDateTime startDate,
        ZonedDateTime endDate,
        String title,
        String description
) {
}

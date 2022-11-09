package hub.event.events.event.dto;

import java.time.ZonedDateTime;

public record EventUpdateDTO(
        Long cityId,
        Long placeId,
        ZonedDateTime startDate,
        ZonedDateTime endDate,
        String title,
        String description
) {
}

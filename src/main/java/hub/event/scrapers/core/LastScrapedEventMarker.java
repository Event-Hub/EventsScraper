package hub.event.scrapers.core;

import java.time.LocalDateTime;

public record LastScrapedEventMarker(String scraperConfigurationName, LocalDateTime eventDate , String eventTitle, String marker) {
}

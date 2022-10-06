package hub.event.scrapers.core.runlog;

import java.time.LocalDateTime;

public record ScraperRunErrorLog(String configurationName, /* Instant */ LocalDateTime time, String errorCode, String description) {
}

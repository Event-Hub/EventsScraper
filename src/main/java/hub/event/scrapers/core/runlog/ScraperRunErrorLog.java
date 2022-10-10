package hub.event.scrapers.core.runlog;

import java.time.Instant;

public record ScraperRunErrorLog(String configurationName, Instant time, String errorCode, String description) {
}

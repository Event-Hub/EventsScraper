package hub.event.scrapers.core.runlog;

import java.time.Instant;

public record ScraperRunStatusLog(String configurationName, Instant startTime, Instant finishTime,
                                  Integer scannedEventCount, Integer errorCount) {
}

package hub.event.scrapers.core.runlog;

import java.time.LocalDateTime;

public record ScraperRunStatusLog(String configurationName, LocalDateTime startTime, LocalDateTime finishTime,
                                  Integer scannedEventCount, Integer errorCount) {
}

package hub.event.scrapers.core.scraper;

import java.time.ZoneId;

public record ScraperConfig(String configurationName, ZoneId timeZone, boolean isActive) {
}

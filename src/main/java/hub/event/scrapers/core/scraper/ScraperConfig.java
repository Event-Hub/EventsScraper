package hub.event.scrapers.core.scraper;

import java.time.ZoneId;

public record ScraperConfig(Integer scraperId, String configurationName, ZoneId timeZone, boolean isActive) {
}

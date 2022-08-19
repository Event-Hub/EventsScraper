package hub.event.scrapers.core;

import hub.event.scrapers.core.datewithlocation.MultipleEventDateWithLocations;
import hub.event.scrapers.core.datewithlocation.SingleEventDateWithLocation;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

class ScrapedEventBuilder {

  private final Map<String, String> metadata;

  ScrapedEventBuilder() {
    this.metadata = new HashMap<>();
  }

  public ScrapedEvent build() {
    return null;
  }

  public ScrapedEventBuilder title(String title) {
    return this;
  }

  public ScrapedEventBuilder description(String description) {
    return this;
  }

  public ScrapedEventBuilder sourceLink(String sourceLink) {
    return this;
  }

  public ScrapedEventBuilder metadata(String key, String value) {
    this.metadata.put(key, value);
    return this;
  }

  public ScrapedEventBuilder date(SingleEventDateWithLocation singleEventDateWithLocation) {
    return this;
  }

  public ScrapedEventBuilder date(MultipleEventDateWithLocations multipleEventDateWithLocations) {
    return this;
  }

  public ScrapedEventBuilder scrapedTime(LocalDateTime scrapedTime) {
    return this;
  }
}

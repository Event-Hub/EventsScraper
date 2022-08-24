package hub.event.scrapers.core;

import hub.event.scrapers.core.datewithlocation.MultipleEventDateWithLocations;
import hub.event.scrapers.core.datewithlocation.SingleEventDateWithLocation;

import java.time.LocalDateTime;
import java.util.*;

class ScrapedEventBuilder {

  private final Map<String, String> metadata;
  private List<String> types;
  private String title;
  private String description;
  private String sourceLink;
  private SingleEventDateWithLocation singleEventDateWithLocation;
  private MultipleEventDateWithLocations multipleEventDateWithLocations;
  private LocalDateTime scrapedTime;

  ScrapedEventBuilder() {
    this.metadata = new HashMap<>();
    this.types = new ArrayList<>();
  }

  public ScrapedEventBuilder title(String title) {
    this.title = title;
    return this;
  }

  public ScrapedEventBuilder description(String description) {
    this.description = description;
    return this;
  }

  public ScrapedEventBuilder sourceLink(String sourceLink) {
    this.sourceLink = sourceLink;
    return this;
  }

  public ScrapedEventBuilder metadata(String key, String value) {
    this.metadata.put(key, value);
    return this;
  }

  public ScrapedEventBuilder date(SingleEventDateWithLocation singleEventDateWithLocation) {
    if (Objects.isNull(multipleEventDateWithLocations)) {
      this.singleEventDateWithLocation = singleEventDateWithLocation;
    }

    return this;
  }

  public ScrapedEventBuilder date(MultipleEventDateWithLocations multipleEventDateWithLocations) {
    this.singleEventDateWithLocation = null;
    this.multipleEventDateWithLocations = multipleEventDateWithLocations;
    return this;
  }

  public ScrapedEventBuilder scrapedTime(LocalDateTime scrapedTime) {
    this.scrapedTime = scrapedTime;
    return this;
  }

  public ScrapedEventBuilder type(String type) {
    this.types.add(type);
    return this;
  }

  public ScrapedEvent build() {
    final UUID eventUuid = UUID.randomUUID();
    return new ScrapedEvent(eventUuid, title, description, sourceLink, singleEventDateWithLocation, multipleEventDateWithLocations, scrapedTime, metadata, types);
  }
}

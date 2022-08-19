package hub.event.scrapers.core;

import hub.event.scrapers.core.datewithlocation.MultipleEventDateWithLocations;
import hub.event.scrapers.core.datewithlocation.SingleEventDateWithLocation;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

public class ScrapedEvent {

  private Map<String, String> metadata;
  private String title;
  private String description;
  private String sourceLink;
  private SingleEventDateWithLocation singleEventDateWithLocation;
  private MultipleEventDateWithLocations multipleEventDateWithLocations;
  private LocalDateTime scrapedTime;

  private ScrapedEvent() {
  }

  public static ScrapedEventBuilder builder() {
    return new ScrapedEventBuilder();
  }

  String title() {
    return title;
  }

  String description() {
    return description;
  }

  String sourceLink() {
    return sourceLink;
  }

  Map<String, String> metadata() {
    return metadata;
  }

  SingleEventDateWithLocation singleEventDateWithLocation() {
    return singleEventDateWithLocation;
  }

  MultipleEventDateWithLocations multipleEventDateWithLocations() {
    return multipleEventDateWithLocations;
  }

  public LocalDateTime scrapedTime() {
    return scrapedTime;
  }

  public boolean hasMultipleDateAndLocations() {
    return Objects.nonNull(multipleEventDateWithLocations);
  }

}

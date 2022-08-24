package hub.event.scrapers.core;

import hub.event.scrapers.core.datewithlocation.MultipleEventDateWithLocations;
import hub.event.scrapers.core.datewithlocation.SingleEventDateWithLocation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class ScrapedEvent {

  private UUID uuid;
  private Map<String, String> metadata;
  private String title;
  private String description;
  private String sourceLink;

  private List<String> types;
  private SingleEventDateWithLocation singleEventDateWithLocation;
  private MultipleEventDateWithLocations multipleEventDateWithLocations;
  private LocalDateTime scrapedTime;

  ScrapedEvent(UUID uuid, String title, String description, String sourceLink,  SingleEventDateWithLocation singleEventDateWithLocation, MultipleEventDateWithLocations multipleEventDateWithLocations, LocalDateTime scrapedTime, Map<String, String> metadata, List<String> types) {
    this.uuid = uuid;
    this.metadata = metadata;
    this.title = title;
    this.description = description;
    this.sourceLink = sourceLink;
    this.types = types;
    this.singleEventDateWithLocation = singleEventDateWithLocation;
    this.multipleEventDateWithLocations = multipleEventDateWithLocations;
    this.scrapedTime = scrapedTime;
  }

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

  LocalDateTime scrapedTime() {
    return scrapedTime;
  }

  boolean hasMultipleDateAndLocations() {
    return Objects.nonNull(multipleEventDateWithLocations);
  }

  UUID uuid() {
    return uuid;
  }

  List<String> types() {
    return types;
  }
}
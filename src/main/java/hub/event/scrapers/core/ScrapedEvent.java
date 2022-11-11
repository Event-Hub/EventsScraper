package hub.event.scrapers.core;

import hub.event.scrapers.core.datewithlocation.MultipleEventDateWithLocations;
import hub.event.scrapers.core.datewithlocation.SingleEventDateWithLocation;

import java.util.*;

public class ScrapedEvent {

  private final Map<String, String> metadata;
  private final String title;
  private final String description;
  private final String sourceLink;
  private final List<String> types;
  private final SingleEventDateWithLocation singleEventDateWithLocation;
  private final MultipleEventDateWithLocations multipleEventDateWithLocations;

  // nowa klasa na powtarzający się event
  // Period - od kiedy do kiedy
  // List<Instant> - lista dat kiedy
  // Interval - co ile się powtarza
  // można na razie nie robić

  private ScrapedEvent(String title, String description, String sourceLink, SingleEventDateWithLocation singleEventDateWithLocation, MultipleEventDateWithLocations multipleEventDateWithLocations, Map<String, String> metadata, List<String> types) {
    this.metadata = metadata;
    this.title = title;
    this.description = description;
    this.sourceLink = sourceLink;
    this.types = types;
    this.singleEventDateWithLocation = singleEventDateWithLocation;
    this.multipleEventDateWithLocations = multipleEventDateWithLocations;
  }

  public static ScrapedEventBuilder builder(SingleEventDateWithLocation singleEventDateWithLocation) {
    return new ScrapedEventBuilder(singleEventDateWithLocation);
  }

  public static ScrapedEventBuilder builder(MultipleEventDateWithLocations multipleEventDateWithLocations) {
    return new ScrapedEventBuilder(multipleEventDateWithLocations);
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

  boolean hasMultipleDateAndLocations() {
    return Objects.nonNull(multipleEventDateWithLocations);
  }

  List<String> types() {
    return types;
  }

  public static class ScrapedEventBuilder {
    private final Map<String, String> metadata;
    private final List<String> types;
    private String title;
    private String description;
    private String sourceLink;
    private SingleEventDateWithLocation singleEventDateWithLocation;
    private MultipleEventDateWithLocations multipleEventDateWithLocations;

    private ScrapedEventBuilder() {
      this.metadata = new HashMap<>();
      this.types = new ArrayList<>();
    }

    public ScrapedEventBuilder(SingleEventDateWithLocation singleEventDateWithLocation) {
      this();
      this.singleEventDateWithLocation = singleEventDateWithLocation;
    }

    public ScrapedEventBuilder(MultipleEventDateWithLocations multipleEventDateWithLocations) {
      this();
      this.multipleEventDateWithLocations = multipleEventDateWithLocations;
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
    public ScrapedEventBuilder type(String type) {
      this.types.add(type);
      return this;
    }

    public ScrapedEvent build() {
      return new ScrapedEvent(title, description, sourceLink, singleEventDateWithLocation, multipleEventDateWithLocations, metadata, types);
    }
  }
}

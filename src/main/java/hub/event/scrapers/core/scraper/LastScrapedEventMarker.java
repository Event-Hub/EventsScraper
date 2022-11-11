package hub.event.scrapers.core.scraper;

import java.time.Instant;
import java.util.Objects;

public class LastScrapedEventMarker {
  private final String scraperConfigurationName;
  private final Instant runDateTime;
  private final String eventTitle;
  private final String marker;
  private final boolean complete;

  public LastScrapedEventMarker(String scraperConfigurationName, Instant runDateTime, String eventTitle, String marker) {
    this(scraperConfigurationName, runDateTime, eventTitle,marker,false);
  }

  public LastScrapedEventMarker(String scraperConfigurationName, Instant runDateTime, String eventTitle, String marker, boolean complete) {
    this.scraperConfigurationName = scraperConfigurationName;
    this.runDateTime = runDateTime;
    this.eventTitle = eventTitle;
    this.marker = marker;
    this.complete = complete;
  }

  public String scraperConfigurationName() {
    return scraperConfigurationName;
  }

  public Instant runDateTime() {
    return runDateTime;
  }

  public String eventTitle() {
    return eventTitle;
  }

  public String marker() {
    return marker;
  }

  public boolean complete() {
    return complete;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) return true;
    if (obj == null || obj.getClass() != this.getClass()) return false;
    var that = (LastScrapedEventMarker) obj;
    return Objects.equals(this.scraperConfigurationName, that.scraperConfigurationName) &&
        Objects.equals(this.runDateTime, that.runDateTime) &&
        Objects.equals(this.eventTitle, that.eventTitle) &&
        Objects.equals(this.marker, that.marker) &&
        this.complete == that.complete;
  }

  @Override
  public int hashCode() {
    return Objects.hash(scraperConfigurationName, runDateTime, eventTitle, marker, complete);
  }

  @Override
  public String toString() {
    return "LastScrapedEventMarker[" +
        "scraperConfigurationName=" + scraperConfigurationName + ", " +
        "runDateTime=" + runDateTime + ", " +
        "eventTitle=" + eventTitle + ", " +
        "marker=" + marker + ", " +
        "complete=" + complete + ']';
  }

}

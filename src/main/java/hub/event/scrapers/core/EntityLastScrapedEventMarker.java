package hub.event.scrapers.core;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

@Entity(name = "scraper_scraped_event_maker")
class EntityLastScrapedEventMarker implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer markerId;
  @Column(nullable = false)
  private Instant runTime;
  private String eventTitle;
  @Column(nullable = false)
  private String marker;
  @Column(nullable = false)
  private Boolean isComplete;
  @Column(nullable = false)
  private Integer scraperId;

  EntityLastScrapedEventMarker() {
  }

  Integer getMarkerId() {
    return markerId;
  }

  void setMarkerId(Integer markerId) {
    this.markerId = markerId;
  }

  Instant getRunTime() {
    return runTime;
  }

  void setRunTime(Instant runTime) {
    this.runTime = runTime;
  }

  String getEventTitle() {
    return eventTitle;
  }

  void setEventTitle(String eventTitle) {
    this.eventTitle = eventTitle;
  }

  String getMarker() {
    return marker;
  }

  void setMarker(String marker) {
    this.marker = marker;
  }

  Boolean getComplete() {
    return isComplete;
  }

  void setComplete(Boolean complete) {
    isComplete = complete;
  }

  Integer getScraperId() {
    return scraperId;
  }

  void setScraperId(Integer scraperId) {
    this.scraperId = scraperId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    EntityLastScrapedEventMarker that = (EntityLastScrapedEventMarker) o;
    return Objects.equals(markerId, that.markerId) && Objects.equals(runTime, that.runTime) && Objects.equals(eventTitle, that.eventTitle) && Objects.equals(marker, that.marker) && Objects.equals(isComplete, that.isComplete) && Objects.equals(scraperId, that.scraperId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(markerId, runTime, eventTitle, marker, isComplete, scraperId);
  }
}

package hub.event.scrapers.core;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import java.io.Serializable;
import java.time.Instant;

@Entity
@IdClass(LastScrapedEventMarkerEntity.class)
class LastScrapedEventMarkerEntity implements Serializable {
  @Id
  private String scraperConfigurationName;
  @Column(nullable = false)
  private Instant runDateTime;
  private String eventTitle;
  @Column(nullable = false)
  private String marker;
  @Id
  private Boolean complete;

  LastScrapedEventMarkerEntity() {
  }

  String getScraperConfigurationName() {
    return scraperConfigurationName;
  }

  void setScraperConfigurationName(String scraperConfigurationName) {
    this.scraperConfigurationName = scraperConfigurationName;
  }

  Instant getRunDateTime() {
    return runDateTime;
  }

  void setRunDateTime(Instant runDateTime) {
    this.runDateTime = runDateTime;
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
    return complete;
  }

  void setComplete(Boolean complete) {
    this.complete = complete;
  }
}

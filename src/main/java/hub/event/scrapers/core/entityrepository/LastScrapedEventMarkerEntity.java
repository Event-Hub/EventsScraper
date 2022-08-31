package hub.event.scrapers.core.entityrepository;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
class LastScrapedEventMarkerEntity {
  @Id
  private String scraperConfigurationName;
  @Column(nullable = false)
  private LocalDateTime runDateTime;
  private String eventTitle;
  @Column(nullable = false)
  private String marker;
  @Column(nullable = false)
  private Boolean complete;

  LastScrapedEventMarkerEntity() {
  }

  public String getScraperConfigurationName() {
    return scraperConfigurationName;
  }

  public void setScraperConfigurationName(String scraperConfigurationName) {
    this.scraperConfigurationName = scraperConfigurationName;
  }

  public LocalDateTime getRunDateTime() {
    return runDateTime;
  }

  public void setRunDateTime(LocalDateTime runDateTime) {
    this.runDateTime = runDateTime;
  }

  public String getEventTitle() {
    return eventTitle;
  }

  public void setEventTitle(String eventTitle) {
    this.eventTitle = eventTitle;
  }

  public String getMarker() {
    return marker;
  }

  public void setMarker(String marker) {
    this.marker = marker;
  }

  public Boolean getComplete() {
    return complete;
  }

  public void setComplete(Boolean complete) {
    this.complete = complete;
  }
}

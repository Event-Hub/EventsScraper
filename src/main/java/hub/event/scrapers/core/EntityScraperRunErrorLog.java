package hub.event.scrapers.core;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

@Entity(name = "Scraper_error_log")
class EntityScraperRunErrorLog implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer logId;

  @Column(nullable = false, name = "error_time")
  private Instant time;

  @Column(nullable = false)
  private String errorCode;
  private String description;
  @Column(nullable = false)
  private Integer scraperId;

  EntityScraperRunErrorLog() {
  }

  EntityScraperRunErrorLog(Integer scraperId, Instant time, String errorCode, String description) {
    this.scraperId = scraperId;
    this.time = time;
    this.errorCode = errorCode;
    this.description = description;
  }

  Integer getLogId() {
    return logId;
  }

  void setLogId(Integer logId) {
    this.logId = logId;
  }

  Instant getTime() {
    return time;
  }

  void setTime(Instant time) {
    this.time = time;
  }

  String getErrorCode() {
    return errorCode;
  }

  void setErrorCode(String errorCode) {
    this.errorCode = errorCode;
  }

  String getDescription() {
    return description;
  }

  void setDescription(String description) {
    this.description = description;
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
    EntityScraperRunErrorLog that = (EntityScraperRunErrorLog) o;
    return Objects.equals(logId, that.logId) && Objects.equals(time, that.time) && Objects.equals(errorCode, that.errorCode) && Objects.equals(description, that.description) && Objects.equals(scraperId, that.scraperId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(logId, time, errorCode, description, scraperId);
  }
}

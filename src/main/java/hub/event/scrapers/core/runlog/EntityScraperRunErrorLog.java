package hub.event.scrapers.core.runlog;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

@Entity(name = "queryScraperErrorLog")
@Table(name = "scraper_error_log")
class EntityScraperRunErrorLog implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer logId;

  @Column(nullable = false, name = "error_time")
  private Instant time;

  @Column(nullable = false)
  private String errorCode;
  private String description;
  @ManyToOne
  @JoinColumn(name = "scraperId", nullable = false, insertable = false, updatable = false)
  @Fetch(FetchMode.JOIN)
  private EntityScraperConfig scraperConfig;

  EntityScraperRunErrorLog() {
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

  void setScraperConfig(EntityScraperConfig scraperConfig) {
    this.scraperConfig = scraperConfig;
  }

  EntityScraperConfig getScraperConfig() {
    return scraperConfig;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    EntityScraperRunErrorLog that = (EntityScraperRunErrorLog) o;
    return Objects.equals(logId, that.logId) && Objects.equals(time, that.time) && Objects.equals(errorCode, that.errorCode) && Objects.equals(description, that.description) && Objects.equals(scraperConfig, that.scraperConfig);
  }

  @Override
  public int hashCode() {
    return Objects.hash(logId, time, errorCode, description, scraperConfig);
  }

  @Override
  public String toString() {
    return "EntityScraperRunErrorLog{" +
        "logId=" + logId +
        ", time=" + time +
        ", errorCode='" + errorCode + '\'' +
        ", description='" + description + '\'' +
        ", scraperConfig=" + scraperConfig +
        '}';
  }

  String getScraperConfigurationName() {
    return scraperConfig.getConfigurationName();
  }
}

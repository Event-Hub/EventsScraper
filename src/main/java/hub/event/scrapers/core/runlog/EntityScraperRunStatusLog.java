package hub.event.scrapers.core.runlog;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

@Entity(name = "queryScraperStatusLog")
@Table(name = "scraper_status_log")
class EntityScraperRunStatusLog implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer logId;
  @Column(nullable = false)
  private Instant startTime;
  @Column(nullable = false)
  private Instant finishTime;
  @Column(nullable = false)
  private Integer scannedEventCount;

  @Column(nullable = false)
  private Integer errorCount;

  @ManyToOne
  @JoinColumn(name = "scraperId", nullable = false, insertable = false, updatable = false)
  private EntityScraperConfig scraperConfig;

  EntityScraperRunStatusLog() {
  }

  Integer getLogId() {
    return logId;
  }

  void setLogId(Integer logId) {
    this.logId = logId;
  }

  Instant getStartTime() {
    return startTime;
  }

  void setStartTime(Instant startTime) {
    this.startTime = startTime;
  }

  Instant getFinishTime() {
    return finishTime;
  }

  void setFinishTime(Instant finishTime) {
    this.finishTime = finishTime;
  }

  Integer getScannedEventCount() {
    return scannedEventCount;
  }

  void setScannedEventCount(Integer scannedEventCount) {
    this.scannedEventCount = scannedEventCount;
  }

  Integer getErrorCount() {
    return errorCount;
  }

  void setErrorCount(Integer errorCount) {
    this.errorCount = errorCount;
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
    EntityScraperRunStatusLog that = (EntityScraperRunStatusLog) o;
    return Objects.equals(logId, that.logId) && Objects.equals(startTime, that.startTime) && Objects.equals(finishTime, that.finishTime) && Objects.equals(scannedEventCount, that.scannedEventCount) && Objects.equals(errorCount, that.errorCount) && Objects.equals(scraperConfig, that.scraperConfig);
  }

  @Override
  public int hashCode() {
    return Objects.hash(logId, startTime, finishTime, scannedEventCount, errorCount, scraperConfig);
  }

  @Override
  public String toString() {
    return "EntityScraperRunStatusLog{" +
        "logId=" + logId +
        ", startTime=" + startTime +
        ", finishTime=" + finishTime +
        ", scannedEventCount=" + scannedEventCount +
        ", errorCount=" + errorCount +
        ", scraperConfig=" + scraperConfig +
        '}';
  }

  String getScraperConfigurationName() {
    return scraperConfig.getConfigurationName();
  }
}

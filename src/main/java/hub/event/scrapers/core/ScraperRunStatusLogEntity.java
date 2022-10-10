package hub.event.scrapers.core;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.Instant;

@Entity
class ScraperRunStatusLogEntity {
  @Id
  private String configurationName;
  @Column(nullable = false)
  private Instant startTime;
  @Column(nullable = false)
  private Instant finishTime;
  @Column(nullable = false)
  private Integer scannedEventCount;
  @Column(nullable = false)
  private Integer errorCount;


  ScraperRunStatusLogEntity(String configurationName, Instant startTime, Instant finishTime, Integer scannedEventCount, Integer errorCount) {
    this.configurationName = configurationName;
    this.startTime = startTime;
    this.finishTime = finishTime;
    this.scannedEventCount = scannedEventCount;
    this.errorCount = errorCount;
  }

  String getConfigurationName() {
    return configurationName;
  }

  void setConfigurationName(String configurationName) {
    this.configurationName = configurationName;
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
}

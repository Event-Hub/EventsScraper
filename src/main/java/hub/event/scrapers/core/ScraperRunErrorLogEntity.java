package hub.event.scrapers.core;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.Instant;

@Entity
class ScraperRunErrorLogEntity {
  @Id
  private String configurationName;
  @Column(nullable = false)
  private Instant time;

  @Column(nullable = false)
  private String errorCode;
  private String description;

  public ScraperRunErrorLogEntity() {
  }

  ScraperRunErrorLogEntity(String configurationName, Instant time, String errorCode, String description) {

    this.configurationName = configurationName;
    this.time = time;
    this.errorCode = errorCode;
    this.description = description;
  }

  String getConfigurationName() {
    return configurationName;
  }

  void setConfigurationName(String configurationName) {
    this.configurationName = configurationName;
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
}

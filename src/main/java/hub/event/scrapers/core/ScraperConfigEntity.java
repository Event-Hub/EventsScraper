package hub.event.scrapers.core;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
class ScraperConfigEntity {
  private String scraperName;
  private String timeZone;
  private boolean activeState;
  @Id
  private String configurationName;
  @Column(nullable = false)
  private boolean isActive;

  public ScraperConfigEntity() {
  }

  public ScraperConfigEntity(String scraperName,String timeZone, boolean activeState) {
    this.scraperName = scraperName;
    this.timeZone = timeZone;
    this.activeState = activeState;
  }

  String getConfigurationName() {
    return configurationName;
  }

  void setConfigurationName(String configurationName) {
    this.configurationName = configurationName;
  }

  String getTimeZone() {
    return timeZone;
  }

  void setTimeZone(String timeZone) {
    this.timeZone = timeZone;
  }

  boolean isActive() {
    return isActive;
  }

  void setActive(boolean active) {
    isActive = active;
  }
}

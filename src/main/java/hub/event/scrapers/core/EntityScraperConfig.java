package hub.event.scrapers.core;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity(name = "scraper_config")
class EntityScraperConfig implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer scraperId;
  @Column(unique = true)
  private String configurationName;
  @Column(nullable = false)
  private String timeZone;
  @Column(nullable = false)
  private boolean isActive;

  EntityScraperConfig() {
  }

  EntityScraperConfig(String configurationName, String timeZone, boolean isActive) {
    this.configurationName = configurationName;
    this.timeZone = timeZone;
    this.isActive = isActive;
  }

  Integer getScraperId() {
    return scraperId;
  }

  void setScraperId(Integer scraperId) {
    this.scraperId = scraperId;
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

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    EntityScraperConfig that = (EntityScraperConfig) o;
    return isActive == that.isActive && Objects.equals(scraperId, that.scraperId) && Objects.equals(configurationName, that.configurationName) && Objects.equals(timeZone, that.timeZone);
  }

  @Override
  public int hashCode() {
    return Objects.hash(scraperId, configurationName, timeZone, isActive);
  }
}

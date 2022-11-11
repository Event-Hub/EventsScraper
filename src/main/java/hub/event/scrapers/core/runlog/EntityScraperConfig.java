package hub.event.scrapers.core.runlog;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity(name = "queryScraperConfig")
@Table(name = "scraper_config")
class EntityScraperConfig implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer scraperId;
  @Column(unique = true)
  private String configurationName;

  EntityScraperConfig() {
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

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    EntityScraperConfig that = (EntityScraperConfig) o;
    return Objects.equals(scraperId, that.scraperId) && Objects.equals(configurationName, that.configurationName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(scraperId, configurationName);
  }

  @Override
  public String toString() {
    return "EntityScraperConfig{" +
        "scraperId=" + scraperId +
        ", configurationName='" + configurationName + '\'' +
        '}';
  }
}

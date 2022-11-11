package hub.event.scrapers.core.exceptions;

public class ScraperConfigurationByNameNotExists extends Exception {
  public ScraperConfigurationByNameNotExists(String scraperConfigurationName) {
    super(String.format("Scraper configuration by name %s not found", scraperConfigurationName));
  }
}

package hub.event.scrapers.core;

import hub.event.scrapers.core.scraper.ScraperConfig;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.ZoneId;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@ActiveProfiles(profiles = "dev")
class ScraperConfigRepositoryTest {
  private final ScraperConfigRepository scraperConfigRepository;

  @Autowired
  ScraperConfigRepositoryTest(ScraperConfigRepository scraperConfigRepository) {
    this.scraperConfigRepository = scraperConfigRepository;
  }

  @Test
  @Order(1)
  void repositoryTest() {
    //given
    final String scraper1Name = "SC1";
    final String scraper2Name = "SC2";
    final String scraper3Name = "SC3";
    final String scraper4Name = "SC4";
    final ZoneId zoneId = ZoneId.systemDefault();

    assertThat(scraperConfigRepository).isNotNull();

    final ScraperConfig createdScraperConfig1 = scraperConfigRepository.create(scraper1Name, zoneId, false);
    final ScraperConfig createdScraperConfig2 = scraperConfigRepository.create(scraper2Name, zoneId, true);
    final ScraperConfig createdScraperConfig3 = scraperConfigRepository.create(scraper3Name, zoneId, false);
    final ScraperConfig createdScraperConfig4 = scraperConfigRepository.create(scraper4Name, zoneId, true);

    assertThatException().isThrownBy(() -> scraperConfigRepository.create(scraper4Name, zoneId, true));

    assertThat(createdScraperConfig1.scraperId()).isPositive();
    assertThat(createdScraperConfig2.scraperId()).isPositive();
    assertThat(createdScraperConfig3.scraperId()).isPositive();
    assertThat(createdScraperConfig4.scraperId()).isPositive();

    assertThat(scraperConfigRepository.exists(createdScraperConfig2.scraperId())).isTrue();
    assertThat(scraperConfigRepository.exists(createdScraperConfig4.scraperId())).isTrue();
    assertThat(scraperConfigRepository.exists(createdScraperConfig4.scraperId() + 1000)).isFalse();

    assertThat(scraperConfigRepository.allScraperConfigs())
        .extracting(ScraperConfig::configurationName, ScraperConfig::timeZone, ScraperConfig::isActive)
        .contains(
            tuple(scraper1Name, zoneId, false),
            tuple(scraper2Name, zoneId, true),
            tuple(scraper3Name, zoneId, false),
            tuple(scraper4Name, zoneId, true)
        );

    scraperConfigRepository.activate(createdScraperConfig3.scraperId());
    scraperConfigRepository.deactivate(createdScraperConfig2.scraperId());

    assertThat(scraperConfigRepository.allScraperConfigs())
        .extracting(ScraperConfig::configurationName, ScraperConfig::timeZone, ScraperConfig::isActive)
        .contains(
            tuple(scraper1Name, zoneId, false),
            tuple(scraper2Name, zoneId, false),
            tuple(scraper3Name, zoneId, true),
            tuple(scraper4Name, zoneId, true)
        ).doesNotContain(
            tuple(scraper2Name, zoneId, true),
            tuple(scraper3Name, zoneId, false)
        );

  }

}
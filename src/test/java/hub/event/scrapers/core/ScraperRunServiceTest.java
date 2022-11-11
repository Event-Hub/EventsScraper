package hub.event.scrapers.core;

import hub.event.scrapers.core.datewithlocation.SingleEventDateWithLocation;
import hub.event.scrapers.core.exceptions.EventDateInPastException;
import hub.event.scrapers.core.scraper.ScraperConfig;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ScraperRunServiceTest {

  @Mock
  private ScraperConfigRepository scraperConfigRepository;
  @Mock
  private EventFacadeAdapter eventFacadeAdapter;
  @Mock
  private LastScrapedEventMarkerRepository lastScrapedEventMarkerRepository;

  @Nested
  class ScrapersStartTest {

    @Test
    void whenListContainsInactiveScraperThenSkipRunIt() {
      //given
      final PageScraperPort activeScraper1 = mock(PageScraperPort.class);
      final PageScraperPort activeScraper2 = mock(PageScraperPort.class);
      final PageScraperPort inactiveScraper = mock(PageScraperPort.class);

      final String activeScraperName1 = "active1";
      final String activeScraperName2 = "active2";
      final String inactiveScraperName = "inactive2";

      final ScraperConfig activeScraper1Config = new ScraperConfig(1, activeScraperName1, ZoneId.systemDefault(), true);
      final ScraperConfig activeScraper2Config = new ScraperConfig(2, activeScraperName2, ZoneId.systemDefault(), true);
      final ScraperConfig inactiveScraperConfig = new ScraperConfig(3, inactiveScraperName, ZoneId.systemDefault(), false);

      final List<PageScraperPort> pageScrapers = Arrays.asList(activeScraper1, activeScraper2, inactiveScraper);
      final List<ScraperConfig> scraperConfigs = new ArrayList<>(Arrays.asList(activeScraper1Config, activeScraper2Config, inactiveScraperConfig));

      final ScraperIdNameCache scraperIdNameCache = new ScraperIdNameCache();
      scraperIdNameCache.add(scraperConfigs);
      final ScraperRunService scraperRunService = new ScraperRunService(scraperConfigRepository, eventFacadeAdapter, lastScrapedEventMarkerRepository, pageScrapers, scraperIdNameCache);

      //when
      when(activeScraper1.configurationName()).thenReturn(activeScraperName1);
      when(activeScraper2.configurationName()).thenReturn(activeScraperName2);
      when(inactiveScraper.configurationName()).thenReturn(inactiveScraperName);

      when(activeScraper1.configurationName()).thenReturn(activeScraperName1);
      when(activeScraper2.configurationName()).thenReturn(activeScraperName2);
      when(inactiveScraper.configurationName()).thenReturn(inactiveScraperName);

      when(scraperConfigRepository.allScraperConfigs()).thenReturn(scraperConfigs);

      //then
      scraperRunService.start();

      verify(scraperConfigRepository, never()).create(anyString(), any(ZoneId.class), anyBoolean());
      verify(activeScraper1).scrap();
      verify(activeScraper2).scrap();
      verify(inactiveScraper, never()).scrap();
    }

    @Test
    void whenScrapedEventExistsThenSaveAsEvent() throws EventDateInPastException {
      //given
      final PageScraperPort activeScraper1 = mock(PageScraperPort.class);
      final PageScraperPort activeScraper2 = mock(PageScraperPort.class);

      final String activeScraperName1 = "active1";
      final String activeScraperName2 = "active2";

      final ScraperConfig activeScraper1Config = new ScraperConfig(10, activeScraperName1, ZoneId.systemDefault(), true);
      final ScraperConfig activeScraper2Config = new ScraperConfig(11, activeScraperName2, ZoneId.systemDefault(), true);
      final List<ScraperConfig> scraperConfigs = new ArrayList<>(Arrays.asList(activeScraper1Config, activeScraper2Config));

      final ScrapedEvent scrapedEvent1 = ScrapedEvent.builder(SingleEventDateWithLocation.single(LocalDate.now().plusDays(1), LocalTime.now().plusHours(2), ZoneId.systemDefault(), "Palaven", "Addres 1", "location 2"))
          .title("Title1")
          .description("Description1")
          .build();
      final ScrapedEvent scrapedEvent2 = ScrapedEvent.builder(SingleEventDateWithLocation.single(LocalDate.now().plusDays(1), LocalTime.now().plusHours(2), ZoneId.systemDefault(), "Thessia", "Addres 2", "location 22"))
          .title("Title2")
          .description("Description2")
          .build();
      final ScrapedEvent scrapedEvent3 = ScrapedEvent.builder(SingleEventDateWithLocation.single(LocalDate.now().plusDays(1), LocalTime.now().plusHours(2), ZoneId.systemDefault(), "Eden Prime", "Addres 134", "location 166"))
          .title("Title1")
          .description("Description1")
          .build();
      final List<ScrapedEvent> scrapedEventList = Arrays.asList(scrapedEvent1, scrapedEvent2, scrapedEvent3);

      final List<PageScraperPort> pageScrapers = Arrays.asList(activeScraper1, activeScraper2);
      final ScraperIdNameCache scraperIdNameCache = new ScraperIdNameCache();
      scraperIdNameCache.add(scraperConfigs);
      final ScraperRunService scraperRunService = new ScraperRunService(scraperConfigRepository, eventFacadeAdapter, lastScrapedEventMarkerRepository, pageScrapers, scraperIdNameCache);

      //when
      when(activeScraper1.configurationName()).thenReturn(activeScraperName1);
      when(activeScraper2.configurationName()).thenReturn(activeScraperName2);

      when(activeScraper1.scrap()).thenReturn(Arrays.asList(scrapedEvent1));
      when(activeScraper2.scrap()).thenReturn(Arrays.asList(scrapedEvent2, scrapedEvent3));

      when(scraperConfigRepository.allScraperConfigs()).thenReturn(scraperConfigs);

      //then
      scraperRunService.start();

      verify(activeScraper1).scrap();
      verify(activeScraper2).scrap();
      verify(eventFacadeAdapter).saveAll(scrapedEventList);
    }

    @Test
    void whenScrapedEventSavedThenMakeLastScrapedEventMarkerDraftActive() throws EventDateInPastException {
      //given
      final PageScraperPort activeScraper1 = mock(PageScraperPort.class);
      final PageScraperPort activeScraper2 = mock(PageScraperPort.class);

      final String activeScraperName1 = "active1";
      final String activeScraperName2 = "active2";

      final ScraperConfig activeScraper1Config = new ScraperConfig(20, activeScraperName1, ZoneId.systemDefault(), true);
      final ScraperConfig activeScraper2Config = new ScraperConfig(21, activeScraperName2, ZoneId.systemDefault(), true);
      final List<ScraperConfig> scraperConfigs = new ArrayList<>(Arrays.asList(activeScraper1Config, activeScraper2Config));

      final ScrapedEvent scrapedEvent1 = ScrapedEvent.builder(SingleEventDateWithLocation.single(LocalDate.now().plusDays(1), LocalTime.now().plusHours(2), ZoneId.systemDefault(), "Palaven", "Addres 1", "location 2"))
          .title("Title1")
          .description("Description1")
          .build();
      final ScrapedEvent scrapedEvent2 = ScrapedEvent.builder(SingleEventDateWithLocation.single(LocalDate.now().plusDays(1), LocalTime.now().plusHours(2), ZoneId.systemDefault(), "Thessia", "Addres 2", "location 22"))
          .title("Title2")
          .description("Description2")
          .build();
      final ScrapedEvent scrapedEvent3 = ScrapedEvent.builder(SingleEventDateWithLocation.single(LocalDate.now().plusDays(1), LocalTime.now().plusHours(2), ZoneId.systemDefault(), "Eden Prime", "Addres 134", "location 166"))
          .title("Title1")
          .description("Description1")
          .build();

      final List<PageScraperPort> pageScrapers = Arrays.asList(activeScraper1, activeScraper2);
      final ScraperIdNameCache scraperIdNameCache = new ScraperIdNameCache();
      scraperIdNameCache.add(scraperConfigs);
      final ScraperRunService scraperRunService = new ScraperRunService(scraperConfigRepository, eventFacadeAdapter, lastScrapedEventMarkerRepository, pageScrapers, scraperIdNameCache);

      //when
      when(activeScraper1.configurationName()).thenReturn(activeScraperName1);
      when(activeScraper2.configurationName()).thenReturn(activeScraperName2);

      when(activeScraper1.scrap()).thenReturn(Arrays.asList(scrapedEvent1));
      when(activeScraper2.scrap()).thenReturn(Arrays.asList(scrapedEvent2, scrapedEvent3));

      when(scraperConfigRepository.allScraperConfigs()).thenReturn(scraperConfigs);

      //then
      scraperRunService.start();

      verify(lastScrapedEventMarkerRepository).setAllAsCompleteByConfigurationsIds(List.of(activeScraper1Config.scraperId(), activeScraper2Config.scraperId()));
    }
  }

  @Nested
  class CreateConfigsIfMissingAndCacheBuildTest {
    @Test
    void whenFoundScrapersWithoutConfigThenCreateDefault() {
      //given
      final PageScraperPort scraper1 = mock(PageScraperPort.class);
      final PageScraperPort scraper2 = mock(PageScraperPort.class);
      final List<PageScraperPort> pageScrapers = Arrays.asList(scraper1, scraper2);

      final ZoneId timeZone = ZoneId.systemDefault();

      final ScraperConfig scraperConfig1 = new ScraperConfig(1, "Scraper1", timeZone, true);
      final ScraperConfig scraperConfig2 = new ScraperConfig(2, "Scraper2", timeZone, true);

      final ScraperRunService scraperRunService = new ScraperRunService(scraperConfigRepository, eventFacadeAdapter, lastScrapedEventMarkerRepository, pageScrapers, new ScraperIdNameCache());
      //when
      when(scraper2.timeZone()).thenReturn(timeZone);
      when(scraper1.configurationName()).thenReturn("Scraper1");
      when(scraper2.configurationName()).thenReturn("Scraper2");
      when(scraperConfigRepository.allScraperConfigs()).thenReturn(List.of(scraperConfig1));
      when(scraperConfigRepository.create("Scraper2", timeZone, true)).thenReturn(scraperConfig2);

      //then
      scraperRunService.createScrapersConfigsIfMissingAndFillIdNameCache();

      verify(scraperConfigRepository).create("Scraper2", timeZone, true);
    }

    @Test
    void cacheContainsFoundedAndCreatedScrapersConfig() {
      //given
      final PageScraperPort scraper1 = mock(PageScraperPort.class);
      final PageScraperPort scraper2 = mock(PageScraperPort.class);
      final List<PageScraperPort> pageScrapers = Arrays.asList(scraper1, scraper2);

      final ZoneId timeZone = ZoneId.systemDefault();

      final ScraperConfig scraperConfig1 = new ScraperConfig(1, "Scraper1", timeZone, true);
      final ScraperConfig scraperConfig2 = new ScraperConfig(2, "Scraper2", timeZone, true);
      final ScraperConfig scraperConfig3 = new ScraperConfig(3, "Scraper3", ZoneId.systemDefault(), true);
      final ScraperConfig scraperConfig4 = new ScraperConfig(4, "Scraper4", ZoneId.systemDefault(), false);
      final ScraperConfig scraperConfig5 = new ScraperConfig(5, "Scraper5", ZoneId.systemDefault(), true);
      final List<ScraperConfig> scraperConfigList = List.of(scraperConfig1, scraperConfig3, scraperConfig4, scraperConfig5);

      final ScraperIdNameCache scraperIdNameCache = new ScraperIdNameCache();
      final ScraperRunService scraperRunService = new ScraperRunService(scraperConfigRepository, eventFacadeAdapter, lastScrapedEventMarkerRepository, pageScrapers, scraperIdNameCache);

      //when
      when(scraper2.timeZone()).thenReturn(timeZone);
      when(scraper1.configurationName()).thenReturn("Scraper1");
      when(scraper2.configurationName()).thenReturn("Scraper2");
      when(scraperConfigRepository.allScraperConfigs()).thenReturn(scraperConfigList);
      when(scraperConfigRepository.create("Scraper2", timeZone, true)).thenReturn(scraperConfig2);

      //then
      scraperRunService.createScrapersConfigsIfMissingAndFillIdNameCache();

      assertEquals(scraperConfig1.scraperId(), scraperIdNameCache.getIdByScraperName(scraperConfig1.configurationName()));
      assertEquals(scraperConfig2.scraperId(), scraperIdNameCache.getIdByScraperName(scraperConfig2.configurationName()));
      assertEquals(scraperConfig3.scraperId(), scraperIdNameCache.getIdByScraperName(scraperConfig3.configurationName()));
      assertEquals(scraperConfig4.scraperId(), scraperIdNameCache.getIdByScraperName(scraperConfig4.configurationName()));
      assertEquals(scraperConfig5.scraperId(), scraperIdNameCache.getIdByScraperName(scraperConfig5.configurationName()));

      assertEquals(scraperConfig1.configurationName(), scraperIdNameCache.getScraperNameById(scraperConfig1.scraperId()));
      assertEquals(scraperConfig2.configurationName(), scraperIdNameCache.getScraperNameById(scraperConfig2.scraperId()));
      assertEquals(scraperConfig3.configurationName(), scraperIdNameCache.getScraperNameById(scraperConfig3.scraperId()));
      assertEquals(scraperConfig4.configurationName(), scraperIdNameCache.getScraperNameById(scraperConfig4.scraperId()));
      assertEquals(scraperConfig5.configurationName(), scraperIdNameCache.getScraperNameById(scraperConfig5.scraperId()));
    }
  }
}
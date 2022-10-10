package hub.event.scrapers.core;

import hub.event.scrapers.core.datewithlocation.SingleEventDateWithLocation;
import hub.event.scrapers.core.exceptions.EventDateInPastException;
import hub.event.scrapers.core.scraper.ScraperConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ScraperRunServiceTest {

  @Mock
  private ScraperConfigRepository scraperConfigRepository;
  @Mock
  private EventFacadeAdapter eventFacadeAdapter;
  @Mock
  private LastScrapedEventMarkerRepository lastScrapedEventMarkerRepository;
  @Captor
  private ArgumentCaptor<List<ScrapedEvent>> scrapedEventListCaptor;

  @Test
  void whenListContainsInactiveScraperThenSkipRunIt() {
    //given
    final PageScraperPort activeScraper1 = mock(PageScraperPort.class);
    final PageScraperPort activeScraper2 = mock(PageScraperPort.class);
    final PageScraperPort inactiveScraper = mock(PageScraperPort.class);

    final String activeScraperName1 = "active1";
    final String activeScraperName2 = "active2";
    final String inactiveScraperName = "inactive2";

    final ScraperConfig activeScraper1Config = new ScraperConfig(activeScraperName1, ZoneId.systemDefault(), true);
    final ScraperConfig activeScraper2Config = new ScraperConfig(activeScraperName2, ZoneId.systemDefault(), true);
    final ScraperConfig inactiveScraperConfig = new ScraperConfig(inactiveScraperName, ZoneId.systemDefault(), false);

    final List<PageScraperPort> pageScrapers = Arrays.asList(activeScraper1, activeScraper2, inactiveScraper);
    final List<ScraperConfig> scraperConfigs = new ArrayList<>(Arrays.asList(activeScraper1Config, activeScraper2Config, inactiveScraperConfig));
    final ScraperRunService scraperRunService = new ScraperRunService(scraperConfigRepository, eventFacadeAdapter, lastScrapedEventMarkerRepository, pageScrapers);

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
  void whenScrapedEventWithoutDuplicatesThenSaveAsEvent() throws EventDateInPastException {
    //given
    final PageScraperPort activeScraper1 = mock(PageScraperPort.class);
    final PageScraperPort activeScraper2 = mock(PageScraperPort.class);

    final String activeScraperName1 = "active1";
    final String activeScraperName2 = "active2";

    final ScraperConfig activeScraper1Config = new ScraperConfig(activeScraperName1, ZoneId.systemDefault(), true);
    final ScraperConfig activeScraper2Config = new ScraperConfig(activeScraperName2, ZoneId.systemDefault(), true);
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
    final ScraperRunService scraperRunService = new ScraperRunService(scraperConfigRepository, eventFacadeAdapter, lastScrapedEventMarkerRepository, pageScrapers);

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

    final ScraperConfig activeScraper1Config = new ScraperConfig(activeScraperName1, ZoneId.systemDefault(), true);
    final ScraperConfig activeScraper2Config = new ScraperConfig(activeScraperName2, ZoneId.systemDefault(), true);
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
    final ScraperRunService scraperRunService = new ScraperRunService(scraperConfigRepository, eventFacadeAdapter, lastScrapedEventMarkerRepository, pageScrapers);

    //when
    when(activeScraper1.configurationName()).thenReturn(activeScraperName1);
    when(activeScraper2.configurationName()).thenReturn(activeScraperName2);

    when(activeScraper1.scrap()).thenReturn(Arrays.asList(scrapedEvent1));
    when(activeScraper2.scrap()).thenReturn(Arrays.asList(scrapedEvent2, scrapedEvent3));

    when(scraperConfigRepository.allScraperConfigs()).thenReturn(scraperConfigs);

    //then
    scraperRunService.start();

    verify(lastScrapedEventMarkerRepository).makeDraftActive(List.of(activeScraperName1, activeScraperName2));
  }
}
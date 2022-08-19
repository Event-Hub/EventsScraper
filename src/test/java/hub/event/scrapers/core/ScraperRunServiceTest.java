package hub.event.scrapers.core;

import hub.event.scrapers.core.datewithlocation.SingleEventDateWithLocation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ScraperRunServiceTest {

  @Mock
  private ScraperConfigRepository scraperConfigRepository;
  @Mock
  private List<PageScraperPort> pageScrapers;
  @Mock
  private EventCandidateAnalyzer eventCandidateAnalyzer;
  @Mock
  private EventFacadeAdapter eventFacadeAdapter;
  @Mock
  private ProbablyDuplicatedEventCandidateRepository probablyDuplicatedEventCandidateRepository;
  @InjectMocks
  private ScraperRunService scraperRunService;
  @Captor
  private ArgumentCaptor<List<ScrapedEvent>> scrapedEventListCaptor;
  private ArgumentCaptor<List<ProbablyDuplicatedEventCandidate>> duplicatedEventCandidateListCaptor;

  @Test
  void whenListContainsScraperWithoutConfigThenCreateNewConfig() {
    //given
    final PageScraperPort activeScraper = mock(PageScraperPort.class);
    final PageScraperPort noConfigScraper1 = mock(PageScraperPort.class);
    final PageScraperPort noConfigScraper2 = mock(PageScraperPort.class);
    final PageScraperPort inactiveScraper = mock(PageScraperPort.class);

    final String activeScraperName = "active1";
    final String inactiveScraperName = "inactive2";
    final String noConfigScraper1Name = "no-config-1";
    final String noConfigScraper2Name = "no-config-2-Atr-45";

    final ScraperConfig activeScraperConfig = new ScraperConfig(activeScraperName, true);
    final ScraperConfig inactiveScraperConfig = new ScraperConfig(inactiveScraperName, false);
    final Collection<ScraperConfig> scraperConfigs = List.of(activeScraperConfig, inactiveScraperConfig);

    //when
    when(activeScraper.configurationName()).thenReturn(activeScraperName);
    when(noConfigScraper1.configurationName()).thenReturn(noConfigScraper1Name);
    when(noConfigScraper2.configurationName()).thenReturn(noConfigScraper2Name);
    when(inactiveScraper.configurationName()).thenReturn(inactiveScraperName);
    when(pageScrapers.stream()).thenReturn(Stream.of(activeScraper, noConfigScraper1, noConfigScraper2, inactiveScraper));
    when(scraperConfigRepository.allScraperConfigs()).thenReturn(scraperConfigs);

    //then
    scraperRunService.start();

    verify(scraperConfigRepository).allScraperConfigs();
    verify(pageScrapers).stream();
    verify(scraperConfigRepository).create(noConfigScraper1Name);
    verify(scraperConfigRepository).create(noConfigScraper2Name);
    verify(scraperConfigRepository, never()).create(activeScraperName);
    verify(scraperConfigRepository, never()).create(inactiveScraperName);
  }

  @Test
  void whenListContainsInactiveScraperThenSkipRunIt() {
    //given
    final PageScraperPort activeScraper1 = mock(PageScraperPort.class);
    final PageScraperPort activeScraper2 = mock(PageScraperPort.class);
    final PageScraperPort inactiveScraper = mock(PageScraperPort.class);

    final String activeScraperName1 = "active1";
    final String activeScraperName2 = "active2";
    final String inactiveScraperName = "inactive2";

    final ScraperConfig activeScraper1Config = new ScraperConfig(activeScraperName1, true);
    final ScraperConfig activeScraper2Config = new ScraperConfig(activeScraperName2, true);
    final ScraperConfig inactiveScraperConfig = new ScraperConfig(inactiveScraperName, false);
    final Collection<ScraperConfig> scraperConfigs = List.of(activeScraper1Config, activeScraper2Config, inactiveScraperConfig);

    //when
    when(activeScraper1.configurationName()).thenReturn(activeScraperName1);
    when(activeScraper2.configurationName()).thenReturn(activeScraperName2);
    when(inactiveScraper.configurationName()).thenReturn(inactiveScraperName);

    when(pageScrapers.stream()).thenReturn(Stream.of(activeScraper1, activeScraper2, inactiveScraper));
    when(scraperConfigRepository.allScraperConfigs()).thenReturn(scraperConfigs);

    //then
    scraperRunService.start();

    verify(scraperConfigRepository).allScraperConfigs();
    verify(pageScrapers).stream();
    verify(scraperConfigRepository, never()).create(anyString());
    verify(activeScraper1).scrap();
    verify(activeScraper2).scrap();
    verify(inactiveScraper, never()).scrap();
  }

  @Test
  void whenScrapedEventWithoutDuplicatesThenSaveAsEvent() {
    //given
    final PageScraperPort activeScraper1 = mock(PageScraperPort.class);
    final PageScraperPort activeScraper2 = mock(PageScraperPort.class);

    final String activeScraperName1 = "active1";
    final String activeScraperName2 = "active2";

    final ScraperConfig activeScraper1Config = new ScraperConfig(activeScraperName1, true);
    final ScraperConfig activeScraper2Config = new ScraperConfig(activeScraperName2, true);
    final Collection<ScraperConfig> scraperConfigs = List.of(activeScraper1Config, activeScraper2Config);

    final ScrapedEvent scrapedEvent1 = ScrapedEvent.builder()
        .title("Title1")
        .description("Description1")
        .scrapedTime(LocalDateTime.now())
        .date(SingleEventDateWithLocation.single(LocalDate.now(), LocalTime.now(), "Palaven", "Addres 1", "location 2"))
        .build();
    final ScrapedEvent scrapedEvent2 = ScrapedEvent.builder()
        .title("Title2")
        .description("Description2")
        .scrapedTime(LocalDateTime.now())
        .date(SingleEventDateWithLocation.single(LocalDate.now(), LocalTime.now(), "Thessia", "Addres 2", "location 22"))
        .build();
    final ScrapedEvent scrapedEvent3 = ScrapedEvent.builder()
        .title("Title1")
        .description("Description1")
        .scrapedTime(LocalDateTime.now())
        .date(SingleEventDateWithLocation.single(LocalDate.now(), LocalTime.now(), "Eden Prime", "Addres 134", "location 166"))
        .build();
    final List<ScrapedEvent> scrapedEventList = List.of(scrapedEvent1, scrapedEvent2, scrapedEvent3);

    final AnalyzedEventCandidate analyzedEventCandidate1 = new AnalyzedEventCandidate(scrapedEvent1);
    final AnalyzedEventCandidate analyzedEventCandidate2 = new AnalyzedEventCandidate(scrapedEvent2);
    final AnalyzedEventCandidate analyzedEventCandidate3 = new AnalyzedEventCandidate(scrapedEvent3);

    //when
    when(activeScraper1.configurationName()).thenReturn(activeScraperName1);
    when(activeScraper2.configurationName()).thenReturn(activeScraperName2);

    when(activeScraper1.scrap()).thenReturn(List.of(scrapedEvent1));
    when(activeScraper2.scrap()).thenReturn(List.of(scrapedEvent2, scrapedEvent3));

    when(pageScrapers.stream()).thenReturn(Stream.of(activeScraper1, activeScraper2));
    when(scraperConfigRepository.allScraperConfigs()).thenReturn(scraperConfigs);

    when(eventCandidateAnalyzer.analyze(scrapedEventList))
        .thenReturn(List.of(analyzedEventCandidate1, analyzedEventCandidate2, analyzedEventCandidate3));

    //then
    scraperRunService.start();

    verify(activeScraper1).scrap();
    verify(activeScraper2).scrap();
    verify(eventCandidateAnalyzer).analyze(scrapedEventList);
    verify(eventFacadeAdapter).saveAll(scrapedEventList);
    verifyNoInteractions(probablyDuplicatedEventCandidateRepository);
  }

  @Test
  void whenScrapedEventContainsDuplicatedCandidateThenSaveAsProbablyDuplicatedEventCandidate() {
    //given
    final PageScraperPort activeScraper1 = mock(PageScraperPort.class);
    final PageScraperPort activeScraper2 = mock(PageScraperPort.class);

    final String activeScraperName1 = "active1";
    final String activeScraperName2 = "active2";

    final ScraperConfig activeScraper1Config = new ScraperConfig(activeScraperName1, true);
    final ScraperConfig activeScraper2Config = new ScraperConfig(activeScraperName2, true);
    final Collection<ScraperConfig> scraperConfigs = List.of(activeScraper1Config, activeScraper2Config);

    final ScrapedEvent scrapedEvent1 = ScrapedEvent.builder()
        .title("Title1")
        .description("Description1")
        .scrapedTime(LocalDateTime.now())
        .date(SingleEventDateWithLocation.single(LocalDate.now(), LocalTime.now(), "Palaven", "Addres 1", "location 2"))
        .build();
    final ScrapedEvent scrapedEvent2 = ScrapedEvent.builder()
        .title("Title2")
        .description("Description2")
        .scrapedTime(LocalDateTime.now())
        .date(SingleEventDateWithLocation.single(LocalDate.now(), LocalTime.now(), "Thessia", "Addres 2", "location 22"))
        .build();
    final ScrapedEvent scrapedEvent3 = ScrapedEvent.builder()
        .title("Title1")
        .description("Description1")
        .scrapedTime(LocalDateTime.now())
        .date(SingleEventDateWithLocation.single(LocalDate.now(), LocalTime.now(), "Palaven", "Addres 1", "location 2"))
        .build();

    final List<ScrapedEvent> scrapedEventList = List.of(scrapedEvent1, scrapedEvent2, scrapedEvent3);

    final AnalyzedEventCandidate analyzedEventCandidate1 = new AnalyzedEventCandidate(scrapedEvent1);
    final AnalyzedEventCandidate analyzedEventCandidate2 = new AnalyzedEventCandidate(scrapedEvent2);
    final AnalyzedEventCandidate analyzedEventCandidate3 = new AnalyzedEventCandidate(scrapedEvent3);

    analyzedEventCandidate1.addDuplicateCandidate(scrapedEvent3);
    analyzedEventCandidate3.addDuplicateCandidate(scrapedEvent1);

    //when
    when(activeScraper1.configurationName()).thenReturn(activeScraperName1);
    when(activeScraper2.configurationName()).thenReturn(activeScraperName2);

    when(activeScraper1.scrap()).thenReturn(List.of(scrapedEvent1));
    when(activeScraper2.scrap()).thenReturn(List.of(scrapedEvent2, scrapedEvent3));

    when(pageScrapers.stream()).thenReturn(Stream.of(activeScraper1, activeScraper2));
    when(scraperConfigRepository.allScraperConfigs()).thenReturn(scraperConfigs);

    when(eventCandidateAnalyzer.analyze(scrapedEventList))
        .thenReturn(List.of(analyzedEventCandidate1, analyzedEventCandidate2, analyzedEventCandidate3));

    //then
    scraperRunService.start();

    verify(activeScraper1).scrap();
    verify(activeScraper2).scrap();
    verify(eventCandidateAnalyzer).analyze(scrapedEventList);
    verify(eventFacadeAdapter).saveAll(scrapedEventListCaptor.capture());
    verify(probablyDuplicatedEventCandidateRepository).saveAll(duplicatedEventCandidateListCaptor.capture());

    assertThat(scrapedEventListCaptor.getValue())
        .hasSize(1)
        .contains(scrapedEvent2)
        .doesNotContain(scrapedEvent1, scrapedEvent3);

    assertThat(duplicatedEventCandidateListCaptor.getValue())
        .hasSize(2)
        .extracting(
            ProbablyDuplicatedEventCandidate::getScrapedEvent,
            eventCandidate -> eventCandidate.getDuplicatedCandidates().size(),
            eventCandidate -> eventCandidate.getDuplicatedEvents().size()
        )
        .contains(
            tuple(scrapedEvent1, 1, 0),
            tuple(scrapedEvent3, 1, 0)
        );

  }

  @Test
  void whenScrapedEventContainsDuplicatedWithEventThenSaveAsProbablyDuplicatedEventCandidate() {
    //given
    final PageScraperPort activeScraper1 = mock(PageScraperPort.class);
    final PageScraperPort activeScraper2 = mock(PageScraperPort.class);

    final String activeScraperName1 = "active1";
    final String activeScraperName2 = "active2";

    final ScraperConfig activeScraper1Config = new ScraperConfig(activeScraperName1, true);
    final ScraperConfig activeScraper2Config = new ScraperConfig(activeScraperName2, true);
    final Collection<ScraperConfig> scraperConfigs = List.of(activeScraper1Config, activeScraper2Config);

    final ScrapedEvent scrapedEvent1 = ScrapedEvent.builder()
        .title("Title1")
        .description("Description1")
        .scrapedTime(LocalDateTime.now())
        .date(SingleEventDateWithLocation.single(LocalDate.now(), LocalTime.now(), "Palaven", "Addres 1", "location 2"))
        .build();
    final ScrapedEvent scrapedEvent2 = ScrapedEvent.builder()
        .title("Title2")
        .description("Description2")
        .scrapedTime(LocalDateTime.now())
        .date(SingleEventDateWithLocation.single(LocalDate.now(), LocalTime.now(), "Thessia", "Addres 2", "location 22"))
        .build();
    final ScrapedEvent scrapedEvent3 = ScrapedEvent.builder()
        .title("Title3")
        .description("Description3")
        .scrapedTime(LocalDateTime.now())
        .date(SingleEventDateWithLocation.single(LocalDate.now(), LocalTime.now(), "Eden Prime", "Addres 133", "location 2"))
        .build();

    final List<ScrapedEvent> scrapedEventList = List.of(scrapedEvent1, scrapedEvent2, scrapedEvent3);

    final AnalyzedEventCandidate analyzedEventCandidate1 = new AnalyzedEventCandidate(scrapedEvent1);
    final AnalyzedEventCandidate analyzedEventCandidate2 = new AnalyzedEventCandidate(scrapedEvent2);
    final AnalyzedEventCandidate analyzedEventCandidate3 = new AnalyzedEventCandidate(scrapedEvent3);

    final ExistsEvent existsEvent = new ExistsEvent(1012,"title","description", null, null);

    analyzedEventCandidate2.addDuplicateEvent(existsEvent);
    //when
    when(activeScraper1.configurationName()).thenReturn(activeScraperName1);
    when(activeScraper2.configurationName()).thenReturn(activeScraperName2);

    when(activeScraper1.scrap()).thenReturn(List.of(scrapedEvent1));
    when(activeScraper2.scrap()).thenReturn(List.of(scrapedEvent2, scrapedEvent3));

    when(pageScrapers.stream()).thenReturn(Stream.of(activeScraper1, activeScraper2));
    when(scraperConfigRepository.allScraperConfigs()).thenReturn(scraperConfigs);

    when(eventCandidateAnalyzer.analyze(scrapedEventList))
        .thenReturn(List.of(analyzedEventCandidate1, analyzedEventCandidate2, analyzedEventCandidate3));

    //then
    scraperRunService.start();

    verify(activeScraper1).scrap();
    verify(activeScraper2).scrap();
    verify(eventCandidateAnalyzer).analyze(scrapedEventList);
    verify(eventFacadeAdapter).saveAll(scrapedEventListCaptor.capture());
    verify(probablyDuplicatedEventCandidateRepository).saveAll(duplicatedEventCandidateListCaptor.capture());

    assertThat(scrapedEventListCaptor.getValue())
        .hasSize(2)
        .contains(scrapedEvent1,scrapedEvent3)
        .doesNotContain(scrapedEvent2);

    assertThat(duplicatedEventCandidateListCaptor.getValue())
        .hasSize(1)
        .extracting(
            ProbablyDuplicatedEventCandidate::getScrapedEvent,
            eventCandidate -> eventCandidate.getDuplicatedCandidates().size(),
            eventCandidate -> eventCandidate.getDuplicatedEvents().size(),
            ProbablyDuplicatedEventCandidate::getScrapedEvent
        )
        .contains(
            tuple(scrapedEvent2, 0, 1, existsEvent)
        );
  }
}
package hub.event.scrapers.core;

import hub.event.scrapers.core.runlog.ScraperRunErrorLog;
import hub.event.scrapers.core.runlog.ScraperRunStatusLog;
import hub.event.scrapers.core.scraper.LastScrapedEventMarker;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PageScraperPortTest {
  @Mock
  private ScraperRunLogRepository scraperRunLogRepository;

  @Mock
  private LastScrapedEventMarkerRepository lastScrapedEventMarkerRepository;

  @Captor
  ArgumentCaptor<LastScrapedEventMarker> lastScrapedEventMarkerArgumentCaptor;

  @InjectMocks
  private PageScraperPort pageScraperPort = new PageScraperPort() {
    @Override
    protected Collection<ScrapedEvent> scrap() {
      return Collections.emptyList();
    }
  };

  @Captor
  private ArgumentCaptor<ScraperRunStatusLog> scraperRunStatusLogArgumentCaptor;
  @Captor
  private ArgumentCaptor<ScraperRunErrorLog> scraperRunErrorLogArgumentCaptor;

  @Test
  void logError() {
    //given
    final String configurationName = pageScraperPort.configurationName();
    final Instant time = Instant.now();
    final String errorCode = "ER1";
    final String description = "Error 1 server is down";

    //then
    pageScraperPort.logError(time, errorCode, description);

    verify(scraperRunLogRepository).save(scraperRunErrorLogArgumentCaptor.capture());

    final ScraperRunErrorLog scraperRunErrorLog = scraperRunErrorLogArgumentCaptor.getValue();

    assertNotNull(scraperRunErrorLog);
    assertEquals(configurationName, scraperRunErrorLog.configurationName());
    assertEquals(time, scraperRunErrorLog.time());
    assertEquals(errorCode, scraperRunErrorLog.errorCode());
    assertEquals(description, scraperRunErrorLog.description());

  }

  @Test
  void logStatus() {
    //given
    final String configurationName = pageScraperPort.configurationName();
    final Instant startTime = Instant.now();
    final Instant finishTime = LocalDateTime.now().plusMinutes(10).toInstant(ZoneOffset.UTC);
    final Integer scannedEventCount = 23;
    final Integer errorCount = 1;


    //then
    pageScraperPort.logStatus(startTime, finishTime, scannedEventCount, errorCount);

    verify(scraperRunLogRepository).save(scraperRunStatusLogArgumentCaptor.capture());

    final ScraperRunStatusLog scraperRunStatusLog = scraperRunStatusLogArgumentCaptor.getValue();

    assertNotNull(scraperRunStatusLog);
    assertEquals(configurationName, scraperRunStatusLog.configurationName());
    assertEquals(startTime, scraperRunStatusLog.startTime());
    assertEquals(finishTime, scraperRunStatusLog.finishTime());
    assertEquals(scannedEventCount, scraperRunStatusLog.scannedEventCount());
    assertEquals(errorCount, scraperRunStatusLog.errorCount());

  }

  @Nested
  class LastScrapedEventMarkerTest {
    @Test
    void whenFoundLastScrapedEventMarkerThenReturnNotEmptyOptional() {
      //given
      final String scraperName = pageScraperPort.configurationName();
      final LastScrapedEventMarker lastScrapedEventMarker = new LastScrapedEventMarker(scraperName, LocalDateTime.now().minusDays(2).toInstant(ZoneOffset.UTC), "Event Title", "Marker", false);
      //when
      when(lastScrapedEventMarkerRepository.findByScraperConfigurationName(scraperName, true))
          .thenReturn(Optional.of(lastScrapedEventMarker));
      //then

      assertDoesNotThrow(() -> {
        final Optional<LastScrapedEventMarker> lastScrapedEventMarkerResult = pageScraperPort.lastScrapedEventMarkerByConfigurationName();
        assertThat(lastScrapedEventMarkerResult)
            .isNotEmpty()
            .get()
            .isEqualTo(lastScrapedEventMarker);
      });

      verify(lastScrapedEventMarkerRepository).findByScraperConfigurationName(scraperName, true);
    }

    @Test
    void whenNotFoundLastScrapedEventMarkerThenReturnOptionalEmpty() {
      //given
      final String scraperName = pageScraperPort.configurationName();
      //when
      when(lastScrapedEventMarkerRepository.findByScraperConfigurationName(scraperName, true))
          .thenReturn(Optional.empty());
      //then

      assertDoesNotThrow(() -> {
        final Optional<LastScrapedEventMarker> lastScrapedEventMarkerResult = pageScraperPort.lastScrapedEventMarkerByConfigurationName();
        assertThat(lastScrapedEventMarkerResult)
            .isEmpty();
      });

      verify(lastScrapedEventMarkerRepository).findByScraperConfigurationName(scraperName, true);
    }

    @Test
    void whenCreateLastScrapedEventMarkerThenReplaceNotCompleteOne() {
      //given
      final String scraperConfigurationName = pageScraperPort.configurationName();
      final LocalDateTime runTime = LocalDateTime.now();

      final LastScrapedEventMarker previousLastScrapedEventMarker = new LastScrapedEventMarker(scraperConfigurationName, runTime.minusDays(1).toInstant(ZoneOffset.UTC), "Title 1", "Marker", false);
      final LastScrapedEventMarker newLastScrapedEventMarker = new LastScrapedEventMarker(scraperConfigurationName, runTime.toInstant(ZoneOffset.UTC), "Title 2", "Marker 2", false);

      //when
      when(lastScrapedEventMarkerRepository.findByScraperConfigurationName(scraperConfigurationName, false))
          .thenReturn(Optional.of(previousLastScrapedEventMarker));

      //then
      pageScraperPort.saveLastScrapedEventMarker(runTime.toInstant(ZoneOffset.UTC), newLastScrapedEventMarker.eventTitle(), newLastScrapedEventMarker.marker());

      verify(lastScrapedEventMarkerRepository).findByScraperConfigurationName(scraperConfigurationName, false);
      verify(lastScrapedEventMarkerRepository).drop(previousLastScrapedEventMarker);

      verify(lastScrapedEventMarkerRepository).store(lastScrapedEventMarkerArgumentCaptor.capture());

      assertThat(lastScrapedEventMarkerArgumentCaptor.getValue())
          .isEqualTo(newLastScrapedEventMarker);

    }
  }

}
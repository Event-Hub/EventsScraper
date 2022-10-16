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
import java.time.ZonedDateTime;
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
  private ScraperLogRepository scraperLogRepository;

  @Mock
  private LastScrapedEventMarkerRepository lastScrapedEventMarkerRepository;
  @Mock
  private ScraperIdNameCache scraperIdNameCache;

  @InjectMocks
  private final PageScraperPort pageScraperPort = new PageScraperPort() {
    @Override
    protected Collection<ScrapedEvent> scrap() {
      return Collections.emptyList();
    }
  };

  @Captor
  ArgumentCaptor<LastScrapedEventMarker> lastScrapedEventMarkerArgumentCaptor;
  @Captor
  private ArgumentCaptor<ScraperRunStatusLog> scraperRunStatusLogArgumentCaptor;
  @Captor
  private ArgumentCaptor<ScraperRunErrorLog> scraperRunErrorLogArgumentCaptor;


  @Nested
  class LogSaveTest {

    @Test
    void logError() {
      //given
      final String configurationName = pageScraperPort.configurationName();
      final Instant time = Instant.now();
      final String errorCode = "ER1";
      final String description = "Error 1 server is down";

      //then
      pageScraperPort.logError(time, errorCode, description);

      verify(scraperLogRepository).save(scraperRunErrorLogArgumentCaptor.capture());

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

      verify(scraperLogRepository).save(scraperRunStatusLogArgumentCaptor.capture());

      final ScraperRunStatusLog scraperRunStatusLog = scraperRunStatusLogArgumentCaptor.getValue();

      assertNotNull(scraperRunStatusLog);
      assertEquals(configurationName, scraperRunStatusLog.configurationName());
      assertEquals(startTime, scraperRunStatusLog.startTime());
      assertEquals(finishTime, scraperRunStatusLog.finishTime());
      assertEquals(scannedEventCount, scraperRunStatusLog.scannedEventCount());
      assertEquals(errorCount, scraperRunStatusLog.errorCount());

    }
  }

  @Nested
  class LastScrapedEventMarkerTest {
    @Test
    void whenFoundLastScrapedEventMarkerThenReturnNotEmptyOptional() {
      //given
      final String scraperName = pageScraperPort.configurationName();
      final int scraperId = 102;
      final LastScrapedEventMarker lastScrapedEventMarker = new LastScrapedEventMarker(scraperName, LocalDateTime.now().minusDays(2).toInstant(ZoneOffset.UTC), "Event Title", "Marker", false);
      //when

      when(scraperIdNameCache.getIdByScraperName(pageScraperPort.configurationName())).thenReturn(scraperId);
      when(lastScrapedEventMarkerRepository.findLastCompletedByScraperConfigurationId(scraperId))
          .thenReturn(Optional.of(lastScrapedEventMarker));
      //then

      assertDoesNotThrow(() -> {
        final Optional<LastScrapedEventMarker> lastScrapedEventMarkerResult = pageScraperPort.lastScrapedEventMarkerByConfigurationName();
        assertThat(lastScrapedEventMarkerResult)
            .isNotEmpty()
            .get()
            .isEqualTo(lastScrapedEventMarker);
      });

      verify(lastScrapedEventMarkerRepository).findLastCompletedByScraperConfigurationId(scraperId);
    }

    @Test
    void whenNotFoundLastScrapedEventMarkerThenReturnOptionalEmpty() {
      //given
      final int scraperId = 103;
      //when
      when(scraperIdNameCache.getIdByScraperName(pageScraperPort.configurationName())).thenReturn(scraperId);
      when(lastScrapedEventMarkerRepository.findLastCompletedByScraperConfigurationId(scraperId))
          .thenReturn(Optional.empty());
      //then

      assertDoesNotThrow(() -> {
        final Optional<LastScrapedEventMarker> lastScrapedEventMarkerResult = pageScraperPort.lastScrapedEventMarkerByConfigurationName();
        assertThat(lastScrapedEventMarkerResult)
            .isEmpty();
      });

      verify(lastScrapedEventMarkerRepository).findLastCompletedByScraperConfigurationId(scraperId);
    }
  }

  @Test
  void saveLastScrapedEventMarker() {
    //given
    final Instant runDateTime = ZonedDateTime.of(LocalDateTime.now(), pageScraperPort.timeZone()).toInstant();
    final String eventTitle = "Test Event Title 1";
    final String marker = "Example marker value for test";

    //then
    assertDoesNotThrow(() -> pageScraperPort.saveLastScrapedEventMarker(runDateTime, eventTitle, marker));

    verify(lastScrapedEventMarkerRepository).store(lastScrapedEventMarkerArgumentCaptor.capture());
    final LastScrapedEventMarker capturedLastScrapedEventMarker = lastScrapedEventMarkerArgumentCaptor.getValue();

    assertEquals(runDateTime, capturedLastScrapedEventMarker.runDateTime());
    assertEquals(eventTitle, capturedLastScrapedEventMarker.eventTitle());
    assertEquals(marker, capturedLastScrapedEventMarker.marker());
  }
}

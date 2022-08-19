package hub.event.scrapers.core.runlog;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ScraperRunLogFacadeTest {

  @Mock
  private ScraperRunLogRepository scraperRunLogRepository;

  @InjectMocks
  private ScraperRunLogFacade scraperRunLogFacade;

  @Captor
  private ArgumentCaptor<ScraperRunStatusLog> scraperRunStatusLogArgumentCaptor;
  @Captor
  private ArgumentCaptor<ScraperRunErrorLog> scraperRunErrorLogArgumentCaptor;

  @Test
  void logError() {
    //given
    final String configurationName = "scraper1";
    final LocalDateTime time = LocalDateTime.now();
    final String errorCode = "ER1";
    final String description = "Error 1 server is down";

    //then
    scraperRunLogFacade.logError(configurationName, time, errorCode, description);

    verify(scraperRunLogRepository).save(scraperRunErrorLogArgumentCaptor.capture());

    final ScraperRunErrorLog scraperRunErrorLog = scraperRunErrorLogArgumentCaptor.capture();

    assertNotNull(scraperRunErrorLog);
    assertEquals(configurationName, scraperRunErrorLog.configurationName());
    assertEquals(time, scraperRunErrorLog.time());
    assertEquals(errorCode, scraperRunErrorLog.errorCode());
    assertEquals(description, scraperRunErrorLog.description());

  }

  @Test
  void logStatus() {
    //given
    final String configurationName = "scraper1";
    final LocalDateTime startTime = LocalDateTime.now();
    final LocalDateTime finishTime = LocalDateTime.now().plusMinutes(10);
    final Integer scannedEventCount = 23;
    final Integer errorCount = 1;


    //then
    scraperRunLogFacade.logStatus(configurationName, startTime, finishTime, scannedEventCount, errorCount);

    verify(scraperRunLogRepository).save(scraperRunStatusLogArgumentCaptor.capture());

    final ScraperRunStatusLog scraperRunStatusLog = scraperRunStatusLogArgumentCaptor.capture();

    assertNotNull(scraperRunStatusLog);
    assertEquals(configurationName, scraperRunStatusLog.configurationName());
    assertEquals(startTime, scraperRunStatusLog.startTime());
    assertEquals(finishTime, scraperRunStatusLog.finishTime());
    assertEquals(scannedEventCount, scraperRunStatusLog.scannedEventCount());
    assertEquals(errorCount, scraperRunStatusLog.errorCount());

  }

}
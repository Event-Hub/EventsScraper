package hub.event.scrapers.core;

import hub.event.scrapers.core.runlog.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles(profiles = "dev")
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = {"/database/scrapers/core/test_data_init.sql"})
class ScraperLogQueryFacadeTest {
  private static final String ZONE_ID = "Europe/Warsaw";
  @Autowired
  private ScraperLogQueryFacade scraperLogQueryFacade;
  @Autowired
  private ScraperLogRepository scraperLogRepository;

  @MockBean
  private ScraperIdNameCache scraperIdNameCache;

  private final String configurationName1 = "Scraper1";
  private final String configurationName2 = "Scraper2";
  private final String configurationName3 = "Scraper3";

  @BeforeEach
  void mockCache() {
    when(scraperIdNameCache.getIdByScraperName(configurationName1)).thenReturn(100000);
    when(scraperIdNameCache.getIdByScraperName(configurationName2)).thenReturn(200000);
    when(scraperIdNameCache.getIdByScraperName(configurationName3)).thenReturn(300000);

    when(scraperIdNameCache.getScraperNameById(100000)).thenReturn(configurationName1);
    when(scraperIdNameCache.getScraperNameById(200000)).thenReturn(configurationName2);
    when(scraperIdNameCache.getScraperNameById(300000)).thenReturn(configurationName3);
  }

  @Nested
  class ErrorLogQueryTest {

    @Test
    void testSearchByEmptyQuery() {
      ErrorLogSearchQuery errorLogSearchQuery = ErrorLogSearchQuery.builder().build();
      assertThat(scraperLogQueryFacade.findAllErrorLog(errorLogSearchQuery))
          .hasSize(9)
          .extracting(
              ScraperRunErrorLog::configurationName,
              ScraperRunErrorLog::description,
              ScraperRunErrorLog::errorCode,
              scraperRunErrorLog -> scraperRunErrorLog.time().toString())
          .contains(
              tuple(configurationName1, "Error 0", "ERR_0", "2022-10-19T21:15:00.015Z"),
              tuple(configurationName1, "Error 0", "ERR_0", "2022-10-20T21:16:00.011Z"),
              tuple(configurationName1, "Error 0", "ERR_0", "2022-10-21T21:17:00.013Z"),
              tuple(configurationName1, "Error 1", "ERR_1", "2022-10-23T21:18:00.022Z"),
              tuple(configurationName1, "Error 1", "ERR_1", "2022-10-24T21:19:00.020Z"),
              tuple(configurationName2, "Error 1", "ERR_1", "2022-10-19T21:20:00.019Z"),
              tuple(configurationName2, "Error 10", "ERR_10", "2022-10-30T22:21:00.011Z"),
              tuple(configurationName3, "Error 1", "ERR_1", "2022-10-19T21:22:00.015Z"),
              tuple(configurationName3, "Error 20", "ERR_20", "2022-10-19T21:23:00.020Z")
          );
    }

    @Test
    void testSearchByDescription() {
      ErrorLogSearchQuery errorLogSearchQuery = ErrorLogSearchQuery.builder()
          .description("Error 0")
          .build();
      assertThat(scraperLogQueryFacade.findAllErrorLog(errorLogSearchQuery))
          .hasSize(3)
          .extracting(
              ScraperRunErrorLog::configurationName, ScraperRunErrorLog::description, ScraperRunErrorLog::errorCode)
          .contains(
              tuple(configurationName1, "Error 0", "ERR_0"),
              tuple(configurationName1, "Error 0", "ERR_0"),
              tuple(configurationName1, "Error 0", "ERR_0")
          );
    }

    @Test
    void testSearchByErrorsCodes() {
      ErrorLogSearchQuery errorLogSearchQuery = ErrorLogSearchQuery.builder()
          .errorCodes(List.of("ERR_10", "ERR_20"))
          .build();
      assertThat(scraperLogQueryFacade.findAllErrorLog(errorLogSearchQuery))
          .hasSize(2)
          .extracting(
              ScraperRunErrorLog::configurationName, ScraperRunErrorLog::description, ScraperRunErrorLog::errorCode)
          .contains(
              tuple(configurationName2, "Error 10", "ERR_10"),
              tuple(configurationName3, "Error 20", "ERR_20")
          );
    }

    @Test
    void testSearchByDateRange() {
      final ZonedDateTime fromDate = ZonedDateTime.of(LocalDateTime.of(2022, 10, 20, 12, 0), ZoneId.of(ZONE_ID));
      final ZonedDateTime toDate = ZonedDateTime.of(LocalDateTime.of(2022, 10, 24, 23, 50), ZoneId.of(ZONE_ID));
      ErrorLogSearchQuery errorLogSearchQuery = ErrorLogSearchQuery.builder()
          .fromDate(fromDate.toInstant())
          .toDate(toDate.toInstant())
          .build();
      assertThat(scraperLogQueryFacade.findAllErrorLog(errorLogSearchQuery))
          .hasSize(4)
          .extracting(
              ScraperRunErrorLog::configurationName,
              ScraperRunErrorLog::description,
              ScraperRunErrorLog::errorCode,
              scraperRunErrorLog -> scraperRunErrorLog.time().toString())
          .contains(
              tuple(configurationName1, "Error 0", "ERR_0", "2022-10-20T21:16:00.011Z"),
              tuple(configurationName1, "Error 0", "ERR_0", "2022-10-21T21:17:00.013Z"),
              tuple(configurationName1, "Error 1", "ERR_1", "2022-10-23T21:18:00.022Z"),
              tuple(configurationName1, "Error 1", "ERR_1", "2022-10-24T21:19:00.020Z")
          );
    }

    @Test
    void testSearchBycConfigurationNames() {
      ErrorLogSearchQuery errorLogSearchQuery = ErrorLogSearchQuery.builder()
          .configurationNames(List.of(configurationName2, configurationName3))
          .build();
      assertThat(scraperLogQueryFacade.findAllErrorLog(errorLogSearchQuery))
          .hasSize(4)
          .extracting(
              ScraperRunErrorLog::configurationName, ScraperRunErrorLog::description, ScraperRunErrorLog::errorCode)
          .contains(
              tuple(configurationName2, "Error 1", "ERR_1"),
              tuple(configurationName2, "Error 10", "ERR_10"),
              tuple(configurationName3, "Error 1", "ERR_1"),
              tuple(configurationName3, "Error 20", "ERR_20")
          );
    }

    @Test
    void testSearchWithPagination() {
      ErrorLogSearchQuery errorLogSearchQuery = ErrorLogSearchQuery.builder()
          .page(2, 2)
          .build();
      assertThat(scraperLogQueryFacade.findAllErrorLog(errorLogSearchQuery))
          .hasSize(2)
          .extracting(
              ScraperRunErrorLog::configurationName, ScraperRunErrorLog::description, ScraperRunErrorLog::errorCode)
          .contains(
              tuple(configurationName1, "Error 1", "ERR_1"),
              tuple(configurationName2, "Error 1", "ERR_1")
          );
    }

    @Test
    void testSearchByMultipleConditionQuery() {
      ErrorLogSearchQuery errorLogSearchQuery = ErrorLogSearchQuery.builder()
          .errorCodes(List.of("ERR_0", "ERR_1"))
          .configurationNames(List.of(configurationName3, configurationName2, configurationName1))
          .fromDate(ZonedDateTime.of(LocalDateTime.of(2022, 1, 1, 0, 0), ZoneId.of(ZONE_ID)).toInstant())
          .toDate(ZonedDateTime.of(LocalDateTime.of(2022, 12, 30, 0, 0), ZoneId.of(ZONE_ID)).toInstant())
          .build();
      assertThat(scraperLogQueryFacade.findAllErrorLog(errorLogSearchQuery))
          .hasSize(7)
          .extracting(
              ScraperRunErrorLog::configurationName, ScraperRunErrorLog::description, ScraperRunErrorLog::errorCode)
          .contains(
              tuple(configurationName1, "Error 0", "ERR_0"),
              tuple(configurationName1, "Error 0", "ERR_0"),
              tuple(configurationName1, "Error 0", "ERR_0"),
              tuple(configurationName1, "Error 1", "ERR_1"),
              tuple(configurationName1, "Error 1", "ERR_1"),
              tuple(configurationName2, "Error 1", "ERR_1"),
              tuple(configurationName3, "Error 1", "ERR_1")
          );
    }
  }

  @Nested
  class StatusLogQueryTest {
    @Test
    void testSearchByEmptyQuery() {
      StatusLogSearchQuery statusLogSearchQuery = StatusLogSearchQuery.builder()
          .build();

      assertThat(scraperLogQueryFacade.findAllStatusLog(statusLogSearchQuery))
          .hasSize(12)
          .extracting(
              ScraperRunStatusLog::configurationName,
              scraperRunStatusLog -> scraperRunStatusLog.startTime().toString(),
              scraperRunStatusLog -> scraperRunStatusLog.finishTime().toString(),
              ScraperRunStatusLog::scannedEventCount,
              ScraperRunStatusLog::errorCount
          )
          .contains(
              tuple("Scraper1", "2022-11-03T19:43:44.735Z", "2022-11-03T19:43:58.794Z", 230, 100),
              tuple("Scraper1", "2022-11-03T19:43:44.735Z", "2022-11-03T19:43:58.794Z", 30, 0),
              tuple("Scraper1", "2022-11-03T19:43:44.735Z", "2022-11-03T19:43:58.794Z", 0, 0),
              tuple("Scraper2", "2022-11-03T19:43:44.735Z", "2022-11-03T19:43:58.794Z", 2, 0),
              tuple("Scraper2", "2022-11-03T19:43:44.735Z", "2022-11-03T19:43:58.794Z", 3, null),
              tuple("Scraper2", "2022-11-03T19:43:44.735Z", "2022-11-03T19:43:58.794Z", 310, 0),
              tuple("Scraper2", "2022-11-03T19:43:44.735Z", "2022-11-03T19:43:58.794Z", 0, 80),
              tuple("Scraper3", "2022-10-04T18:43:44.735Z", "2022-10-04T18:43:58.794Z", 230, 100),
              tuple("Scraper3", "2022-10-05T18:43:44.735Z", "2022-10-05T18:43:58.794Z", 230, 100),
              tuple("Scraper3", "2022-11-01T19:43:44.735Z", "2022-11-01T19:43:58.794Z", 230, 100),
              tuple("Scraper3", "2022-11-02T19:43:44.735Z", "2022-11-02T19:43:58.794Z", 230, 100),
              tuple("Scraper3", "2022-11-03T19:43:44.735Z", "2022-11-03T19:43:58.794Z", 230, 100)
          );
    }

    @Test
    void testSearchByStartDateQuery() {
      StatusLogSearchQuery statusLogSearchQuery = StatusLogSearchQuery.builder()
          .startTimeFrom(ZonedDateTime.of(LocalDateTime.of(2022, 10, 1, 0, 0), ZoneId.of(ZONE_ID)).toInstant())
          .startTimeTo(ZonedDateTime.of(LocalDateTime.of(2022, 10, 5, 0, 0), ZoneId.of(ZONE_ID)).toInstant())
          .build();

      assertThat(scraperLogQueryFacade.findAllStatusLog(statusLogSearchQuery))
          .hasSize(1)
          .extracting(
              ScraperRunStatusLog::configurationName,
              scraperRunStatusLog -> scraperRunStatusLog.startTime().toString(),
              scraperRunStatusLog -> scraperRunStatusLog.finishTime().toString(),
              ScraperRunStatusLog::scannedEventCount,
              ScraperRunStatusLog::errorCount
          )
          .contains(
              tuple("Scraper3", "2022-10-04T18:43:44.735Z", "2022-10-04T18:43:58.794Z", 230, 100)
          );
    }

    @Test
    void testSearchByFinishDateQuery() {
      StatusLogSearchQuery statusLogSearchQuery = StatusLogSearchQuery.builder()
          .finishTimeFrom(ZonedDateTime.of(LocalDateTime.of(2022, 10, 1, 0, 0), ZoneId.of(ZONE_ID)).toInstant())
          .finishTimeTo(ZonedDateTime.of(LocalDateTime.of(2022, 10, 5, 0, 0), ZoneId.of(ZONE_ID)).toInstant())
          .build();

      assertThat(scraperLogQueryFacade.findAllStatusLog(statusLogSearchQuery))
          .hasSize(1)
          .extracting(
              ScraperRunStatusLog::configurationName,
              scraperRunStatusLog -> scraperRunStatusLog.startTime().toString(),
              scraperRunStatusLog -> scraperRunStatusLog.finishTime().toString(),
              ScraperRunStatusLog::scannedEventCount,
              ScraperRunStatusLog::errorCount
          )
          .contains(
              tuple("Scraper3", "2022-10-04T18:43:44.735Z", "2022-10-04T18:43:58.794Z", 230, 100)
          );
    }

    @Test
    void testSearchByNamesQuery() {
      StatusLogSearchQuery statusLogSearchQuery = StatusLogSearchQuery.builder()
          .configurationNames(List.of(configurationName1, configurationName3))
          .build();

      assertThat(scraperLogQueryFacade.findAllStatusLog(statusLogSearchQuery))
          .hasSize(8)
          .extracting(
              ScraperRunStatusLog::configurationName,
              scraperRunStatusLog -> scraperRunStatusLog.startTime().toString(),
              scraperRunStatusLog -> scraperRunStatusLog.finishTime().toString(),
              ScraperRunStatusLog::scannedEventCount,
              ScraperRunStatusLog::errorCount
          )
          .contains(
              tuple("Scraper1", "2022-11-03T19:43:44.735Z", "2022-11-03T19:43:58.794Z", 230, 100),
              tuple("Scraper1", "2022-11-03T19:43:44.735Z", "2022-11-03T19:43:58.794Z", 30, 0),
              tuple("Scraper1", "2022-11-03T19:43:44.735Z", "2022-11-03T19:43:58.794Z", 0, 0),
              tuple("Scraper3", "2022-10-04T18:43:44.735Z", "2022-10-04T18:43:58.794Z", 230, 100),
              tuple("Scraper3", "2022-10-05T18:43:44.735Z", "2022-10-05T18:43:58.794Z", 230, 100),
              tuple("Scraper3", "2022-11-01T19:43:44.735Z", "2022-11-01T19:43:58.794Z", 230, 100),
              tuple("Scraper3", "2022-11-02T19:43:44.735Z", "2022-11-02T19:43:58.794Z", 230, 100),
              tuple("Scraper3", "2022-11-03T19:43:44.735Z", "2022-11-03T19:43:58.794Z", 230, 100)
          );
    }


    @Test
    void testSearchWithPaginationQuery() {
      StatusLogSearchQuery statusLogSearchQuery = StatusLogSearchQuery.builder()
          .page(2, 3)
          .build();

      assertThat(scraperLogQueryFacade.findAllStatusLog(statusLogSearchQuery))
          .hasSize(3)
          .extracting(
              ScraperRunStatusLog::configurationName,
              scraperRunStatusLog -> scraperRunStatusLog.startTime().toString(),
              scraperRunStatusLog -> scraperRunStatusLog.finishTime().toString(),
              ScraperRunStatusLog::scannedEventCount,
              ScraperRunStatusLog::errorCount
          )
          .contains(
              tuple("Scraper2", "2022-11-03T19:43:44.735Z", "2022-11-03T19:43:58.794Z", 0, 80),
              tuple("Scraper3", "2022-10-04T18:43:44.735Z", "2022-10-04T18:43:58.794Z", 230, 100),
              tuple("Scraper3", "2022-10-05T18:43:44.735Z", "2022-10-05T18:43:58.794Z", 230, 100)
          );
    }
  }

  @Nested
  class StatusLogErrorQueryTest {
    @Test
    void testSearchByErrorCountQuery() {
      StatusLogSearchQuery statusLogSearchQuery = StatusLogSearchQuery.builder()
          .errorCountGreaterThanOrEqualTo(75)
          .build();

      assertThat(scraperLogQueryFacade.findAllStatusLog(statusLogSearchQuery))
          .hasSize(7)
          .extracting(
              ScraperRunStatusLog::configurationName,
              scraperRunStatusLog -> scraperRunStatusLog.startTime().toString(),
              scraperRunStatusLog -> scraperRunStatusLog.finishTime().toString(),
              ScraperRunStatusLog::scannedEventCount,
              ScraperRunStatusLog::errorCount
          )
          .contains(
              tuple("Scraper1", "2022-11-03T19:43:44.735Z", "2022-11-03T19:43:58.794Z", 230, 100),
              tuple("Scraper2", "2022-11-03T19:43:44.735Z", "2022-11-03T19:43:58.794Z", 0, 80),
              tuple("Scraper3", "2022-10-04T18:43:44.735Z", "2022-10-04T18:43:58.794Z", 230, 100),
              tuple("Scraper3", "2022-10-05T18:43:44.735Z", "2022-10-05T18:43:58.794Z", 230, 100),
              tuple("Scraper3", "2022-11-01T19:43:44.735Z", "2022-11-01T19:43:58.794Z", 230, 100),
              tuple("Scraper3", "2022-11-02T19:43:44.735Z", "2022-11-02T19:43:58.794Z", 230, 100),
              tuple("Scraper3", "2022-11-03T19:43:44.735Z", "2022-11-03T19:43:58.794Z", 230, 100)
          );
    }

    @Test
    void testSearchByHasErrorQuery() {
      StatusLogSearchQuery statusLogSearchQuery = StatusLogSearchQuery.builder()
          .hasErrors(true)
          .build();

      assertThat(scraperLogQueryFacade.findAllStatusLog(statusLogSearchQuery))
          .hasSize(7)
          .extracting(
              ScraperRunStatusLog::configurationName,
              scraperRunStatusLog -> scraperRunStatusLog.startTime().toString(),
              scraperRunStatusLog -> scraperRunStatusLog.finishTime().toString(),
              ScraperRunStatusLog::scannedEventCount,
              ScraperRunStatusLog::errorCount
          )
          .contains(
              tuple("Scraper1", "2022-11-03T19:43:44.735Z", "2022-11-03T19:43:58.794Z", 230, 100),
              tuple("Scraper2", "2022-11-03T19:43:44.735Z", "2022-11-03T19:43:58.794Z", 0, 80),
              tuple("Scraper3", "2022-10-04T18:43:44.735Z", "2022-10-04T18:43:58.794Z", 230, 100),
              tuple("Scraper3", "2022-10-05T18:43:44.735Z", "2022-10-05T18:43:58.794Z", 230, 100),
              tuple("Scraper3", "2022-11-01T19:43:44.735Z", "2022-11-01T19:43:58.794Z", 230, 100),
              tuple("Scraper3", "2022-11-02T19:43:44.735Z", "2022-11-02T19:43:58.794Z", 230, 100),
              tuple("Scraper3", "2022-11-03T19:43:44.735Z", "2022-11-03T19:43:58.794Z", 230, 100)
          );
    }

    @Test
    void testSearchByNotHasErrorsQuery() {
      StatusLogSearchQuery statusLogSearchQuery = StatusLogSearchQuery.builder()
          .hasErrors(false)
          .build();

      assertThat(scraperLogQueryFacade.findAllStatusLog(statusLogSearchQuery))
          .hasSize(5)
          .extracting(
              ScraperRunStatusLog::configurationName,
              scraperRunStatusLog -> scraperRunStatusLog.startTime().toString(),
              scraperRunStatusLog -> scraperRunStatusLog.finishTime().toString(),
              ScraperRunStatusLog::scannedEventCount,
              ScraperRunStatusLog::errorCount
          )
          .contains(
              tuple("Scraper1", "2022-11-03T19:43:44.735Z", "2022-11-03T19:43:58.794Z", 30, 0),
              tuple("Scraper1", "2022-11-03T19:43:44.735Z", "2022-11-03T19:43:58.794Z", 0, 0),
              tuple("Scraper2", "2022-11-03T19:43:44.735Z", "2022-11-03T19:43:58.794Z", 2, 0),
              tuple("Scraper2", "2022-11-03T19:43:44.735Z", "2022-11-03T19:43:58.794Z", 3, null),
              tuple("Scraper2", "2022-11-03T19:43:44.735Z", "2022-11-03T19:43:58.794Z", 310, 0)
          );
    }
  }

  @Nested
  class StatusLogScannedEventQueryTest {
    @Test
    void testSearchByScannedEventCountQuery() {
      StatusLogSearchQuery statusLogSearchQuery = StatusLogSearchQuery.builder()
          .scannedEventGreaterThanOrEqualTo(30)
          .build();

      assertThat(scraperLogQueryFacade.findAllStatusLog(statusLogSearchQuery))
          .hasSize(8)
          .extracting(
              ScraperRunStatusLog::configurationName,
              scraperRunStatusLog -> scraperRunStatusLog.startTime().toString(),
              scraperRunStatusLog -> scraperRunStatusLog.finishTime().toString(),
              ScraperRunStatusLog::scannedEventCount,
              ScraperRunStatusLog::errorCount
          )
          .contains(
              tuple("Scraper1", "2022-11-03T19:43:44.735Z", "2022-11-03T19:43:58.794Z", 230, 100),
              tuple("Scraper1", "2022-11-03T19:43:44.735Z", "2022-11-03T19:43:58.794Z", 30, 0),
              tuple("Scraper2", "2022-11-03T19:43:44.735Z", "2022-11-03T19:43:58.794Z", 310, 0),
              tuple("Scraper3", "2022-10-04T18:43:44.735Z", "2022-10-04T18:43:58.794Z", 230, 100),
              tuple("Scraper3", "2022-10-05T18:43:44.735Z", "2022-10-05T18:43:58.794Z", 230, 100),
              tuple("Scraper3", "2022-11-01T19:43:44.735Z", "2022-11-01T19:43:58.794Z", 230, 100),
              tuple("Scraper3", "2022-11-02T19:43:44.735Z", "2022-11-02T19:43:58.794Z", 230, 100),
              tuple("Scraper3", "2022-11-03T19:43:44.735Z", "2022-11-03T19:43:58.794Z", 230, 100)
          );
    }

    @Test
    void testSearchByHasScannedEventQuery() {
      StatusLogSearchQuery statusLogSearchQuery = StatusLogSearchQuery.builder()
          .hasScannedEvent(true)
          .build();

      assertThat(scraperLogQueryFacade.findAllStatusLog(statusLogSearchQuery))
          .hasSize(10)
          .extracting(
              ScraperRunStatusLog::configurationName,
              scraperRunStatusLog -> scraperRunStatusLog.startTime().toString(),
              scraperRunStatusLog -> scraperRunStatusLog.finishTime().toString(),
              ScraperRunStatusLog::scannedEventCount,
              ScraperRunStatusLog::errorCount
          )
          .contains(
              tuple("Scraper1", "2022-11-03T19:43:44.735Z", "2022-11-03T19:43:58.794Z", 230, 100),
              tuple("Scraper1", "2022-11-03T19:43:44.735Z", "2022-11-03T19:43:58.794Z", 30, 0),
              tuple("Scraper2", "2022-11-03T19:43:44.735Z", "2022-11-03T19:43:58.794Z", 2, 0),
              tuple("Scraper2", "2022-11-03T19:43:44.735Z", "2022-11-03T19:43:58.794Z", 3, null),
              tuple("Scraper2", "2022-11-03T19:43:44.735Z", "2022-11-03T19:43:58.794Z", 310, 0),
              tuple("Scraper3", "2022-10-04T18:43:44.735Z", "2022-10-04T18:43:58.794Z", 230, 100),
              tuple("Scraper3", "2022-10-05T18:43:44.735Z", "2022-10-05T18:43:58.794Z", 230, 100),
              tuple("Scraper3", "2022-11-01T19:43:44.735Z", "2022-11-01T19:43:58.794Z", 230, 100),
              tuple("Scraper3", "2022-11-02T19:43:44.735Z", "2022-11-02T19:43:58.794Z", 230, 100),
              tuple("Scraper3", "2022-11-03T19:43:44.735Z", "2022-11-03T19:43:58.794Z", 230, 100)
          );
    }

    @Test
    void testSearchByNotHasScannedEventQuery() {
      StatusLogSearchQuery statusLogSearchQuery = StatusLogSearchQuery.builder()
          .hasScannedEvent(false)
          .build();

      assertThat(scraperLogQueryFacade.findAllStatusLog(statusLogSearchQuery))
          .hasSize(2)
          .extracting(
              ScraperRunStatusLog::configurationName,
              scraperRunStatusLog -> scraperRunStatusLog.startTime().toString(),
              scraperRunStatusLog -> scraperRunStatusLog.finishTime().toString(),
              ScraperRunStatusLog::scannedEventCount,
              ScraperRunStatusLog::errorCount
          )
          .contains(
              tuple("Scraper1", "2022-11-03T19:43:44.735Z", "2022-11-03T19:43:58.794Z", 0, 0),
              tuple("Scraper2", "2022-11-03T19:43:44.735Z", "2022-11-03T19:43:58.794Z", 0, 80)
          );
    }

  }
}
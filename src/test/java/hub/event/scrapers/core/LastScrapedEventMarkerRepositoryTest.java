package hub.event.scrapers.core;

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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LastScrapedEventMarkerRepositoryTest {

  @Mock
  private JpaLastScrapedEventMarkerRepository lastScrapedEventMarkerEntityRepository;
  @Mock
  private ScraperIdNameCache scraperIdNameCache;
  @InjectMocks
  private LastScrapedEventMarkerRepository lastScrapedEventMarkerRepository;

  @Captor
  ArgumentCaptor<EntityLastScrapedEventMarker> lastScrapedEventMarkerEntityCaptor;

  @Test
  void saveTest() {
    //given
    LastScrapedEventMarker eventMarker1 = new LastScrapedEventMarker("config1", LocalDateTime.now().toInstant(ZoneOffset.UTC), "title1", "marker1");
    LastScrapedEventMarker eventMarker2 = new LastScrapedEventMarker("config2", LocalDateTime.now().toInstant(ZoneOffset.UTC), "title2", "marker2");

    //when
    when(scraperIdNameCache.getIdByScraperName("config1")).thenReturn(20);
    when(scraperIdNameCache.getIdByScraperName("config2")).thenReturn(40);

    //then
    lastScrapedEventMarkerRepository.store(eventMarker1);
    lastScrapedEventMarkerRepository.store(eventMarker2);

    verify(lastScrapedEventMarkerEntityRepository, times(2)).save(lastScrapedEventMarkerEntityCaptor.capture());

    assertThat(lastScrapedEventMarkerEntityCaptor.getAllValues())
        .extracting(
            EntityLastScrapedEventMarker::getScraperId,
            EntityLastScrapedEventMarker::getRunTime,
            EntityLastScrapedEventMarker::getEventTitle,
            EntityLastScrapedEventMarker::getMarker
        ).contains(
            tuple(20, eventMarker1.runDateTime(), eventMarker1.eventTitle(), eventMarker1.marker()),
            tuple(40, eventMarker2.runDateTime(), eventMarker2.eventTitle(), eventMarker2.marker())
        );

  }

  @Nested
  class FindByScraperConfigurationNameTest {

    @Test
    void whenNotExistsThenReturnEmptyMarker() {
      //given
      final int scraperId = 4560;

      //when
      when(lastScrapedEventMarkerEntityRepository.findByScraperId(scraperId))
          .thenReturn(Optional.empty());

      //then
      final Optional<LastScrapedEventMarker> lastScrapedEventMarker = lastScrapedEventMarkerRepository.findLastCompletedByScraperConfigurationId(scraperId);

      assertThat(lastScrapedEventMarker).isEmpty();
      verify(lastScrapedEventMarkerEntityRepository).findByScraperId(scraperId);
    }

    @Test
    void whenExistsThenReturnMarker() {
      //given
      final String scraperConfigurationName = "exists_scraper";
      final Instant localDateTime = LocalDateTime.now().toInstant(ZoneOffset.UTC);
      final int scraperId = 4570;

      final EntityLastScrapedEventMarker entityLastScrapedEventMarker = new EntityLastScrapedEventMarker();
      entityLastScrapedEventMarker.setMarker("maker1");
      entityLastScrapedEventMarker.setRunTime(localDateTime);
      entityLastScrapedEventMarker.setEventTitle("title1");
      entityLastScrapedEventMarker.setScraperId(scraperId);
      entityLastScrapedEventMarker.setComplete(true);

      //when
      when(scraperIdNameCache.getScraperNameById(scraperId)).thenReturn(scraperConfigurationName);
      when(lastScrapedEventMarkerEntityRepository.findByScraperId(scraperId))
          .thenReturn(Optional.of(entityLastScrapedEventMarker));

      //then
      final Optional<LastScrapedEventMarker> lastScrapedEventMarker = lastScrapedEventMarkerRepository.findLastCompletedByScraperConfigurationId(scraperId);

      assertThat(lastScrapedEventMarker).isNotEmpty()
          .get()
          .extracting(
              LastScrapedEventMarker::scraperConfigurationName,
              LastScrapedEventMarker::runDateTime,
              LastScrapedEventMarker::eventTitle,
              LastScrapedEventMarker::marker
          )
          .contains(scraperConfigurationName, localDateTime, "title1", "maker1");

      verify(lastScrapedEventMarkerEntityRepository).findByScraperId(scraperId);
    }
  }
}
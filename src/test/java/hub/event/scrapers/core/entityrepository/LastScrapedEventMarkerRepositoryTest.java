package hub.event.scrapers.core.entityrepository;

import hub.event.scrapers.core.LastScrapedEventMarker;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LastScrapedEventMarkerRepositoryTest {

  @Mock
  private LastScrapedEventMarkerJpaRepository lastScrapedEventMarkerEntityRepository;
  @InjectMocks
  private LastScrapedEventMarkerEntityRepository lastScrapedEventMarkerRepository;

  @Captor
  ArgumentCaptor<LastScrapedEventMarkerEntity> lastScrapedEventMarkerEntityCaptor;

  @Test
  void saveTest() {
    //given
    LastScrapedEventMarker eventMarker1 = new LastScrapedEventMarker("config1", LocalDateTime.now(), "title1", "marker1");
    LastScrapedEventMarker eventMarker2 = new LastScrapedEventMarker("config2", LocalDateTime.now(), "title2", "marker2");


    //then
    lastScrapedEventMarkerRepository.store(eventMarker1);
    lastScrapedEventMarkerRepository.store(eventMarker2);

    verify(lastScrapedEventMarkerEntityRepository, times(2)).save(lastScrapedEventMarkerEntityCaptor.capture());

    assertThat(lastScrapedEventMarkerEntityCaptor.getAllValues())
        .extracting(
            LastScrapedEventMarkerEntity::getScraperConfigurationName,
            LastScrapedEventMarkerEntity::getEventDate,
            LastScrapedEventMarkerEntity::getEventTitle,
            LastScrapedEventMarkerEntity::getMarker
        ).contains(
            tuple(eventMarker1.scraperConfigurationName(), eventMarker1.eventDate(), eventMarker1.eventTitle(), eventMarker1.marker()),
            tuple(eventMarker2.scraperConfigurationName(), eventMarker2.eventDate(), eventMarker2.eventTitle(), eventMarker2.marker())
        );

  }

  @Test
  void whenFindByNotExistsScraperConfigurationNameThenReturnEmptyMarker() {
    //given
    final String scraperConfigurationName = "not_exists_scraper";

    //when
    when(lastScrapedEventMarkerEntityRepository.findByScraperConfigurationName(scraperConfigurationName))
        .thenReturn(Optional.empty());

    //then
    final Optional<LastScrapedEventMarker> lastScrapedEventMarker = lastScrapedEventMarkerRepository.findByScraperConfigurationName(scraperConfigurationName);

    assertThat(lastScrapedEventMarker).isEmpty();
    verify(lastScrapedEventMarkerEntityRepository).findByScraperConfigurationName(scraperConfigurationName);
  }

  @Test
  void whenFindByExistsScraperConfigurationNameThenReturnMarker() {
    //given
    final String scraperConfigurationName = "exists_scraper";
    final LocalDateTime localDateTime = LocalDateTime.now();

    final LastScrapedEventMarkerEntity lastScrapedEventMarkerEntity = new LastScrapedEventMarkerEntity();
    lastScrapedEventMarkerEntity.setMarker("maker1");
    lastScrapedEventMarkerEntity.setEventDate(localDateTime);
    lastScrapedEventMarkerEntity.setEventTitle("title1");
    lastScrapedEventMarkerEntity.setScraperConfigurationName("exists_scraper");

    //when
    when(lastScrapedEventMarkerEntityRepository.findByScraperConfigurationName(scraperConfigurationName))
        .thenReturn(Optional.of(lastScrapedEventMarkerEntity));

    //then
    final Optional<LastScrapedEventMarker> lastScrapedEventMarker = lastScrapedEventMarkerRepository.findByScraperConfigurationName(scraperConfigurationName);

    assertThat(lastScrapedEventMarker).isNotEmpty()
        .get()
        .extracting(
            LastScrapedEventMarker::scraperConfigurationName,
            LastScrapedEventMarker::eventDate,
            LastScrapedEventMarker::eventTitle,
            LastScrapedEventMarker::marker
        )
        .contains(scraperConfigurationName, localDateTime, "title1", "maker1");

    verify(lastScrapedEventMarkerEntityRepository).findByScraperConfigurationName(scraperConfigurationName);
  }
}
package hub.event.scrapers.core;

import hub.event.scrapers.core.exceptions.ScraperConfigurationByNameNotExists;
import org.junit.jupiter.api.Nested;
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
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ScraperFacadeTest {
  @Mock
  private LastScrapedEventMarkerRepository lastScrapedEventMarkerRepository;
  @Mock
  private ScraperConfigRepository scraperConfigRepository;

  @Captor
  ArgumentCaptor<LastScrapedEventMarker> lastScrapedEventMarkerArgumentCaptor;
  @InjectMocks
  private ScraperFacade scraperFacade;

  @Nested
  class ScraperConfigTest {
    @Test
    void whenActivateScraperThatNotExistThenCreateNewRecord() {
      //given
      final String scraperName = "sc1";
      //when
      when(scraperConfigRepository.exists(scraperName)).thenReturn(false);
      //then
      scraperFacade.activateScraperByConfigurationName(scraperName);

      verify(scraperConfigRepository).exists(scraperName);
      verify(scraperConfigRepository).create(scraperName, true);
      verify(scraperConfigRepository, never()).activate(scraperName);
    }

    @Test
    void whenActivateScraperThatExistThenFinishSuccessfully() {
      //given
      final String scraperName = "sc4";
      //when
      when(scraperConfigRepository.exists(scraperName)).thenReturn(true);
      //then
      scraperFacade.activateScraperByConfigurationName(scraperName);

      verify(scraperConfigRepository).exists(scraperName);
      verify(scraperConfigRepository, never()).create(scraperName, true);
      verify(scraperConfigRepository).activate(scraperName);
    }

    @Test
    void whenDeactivateNotExistsScraperThanThrowConfigNotExistsException() {
      //given
      final String scraperName = "sc2";
      //when
      when(scraperConfigRepository.exists(scraperName)).thenReturn(false);
      //then
      assertThrows(ScraperConfigurationByNameNotExists.class, () -> scraperFacade.deactivateScraperByConfigurationName(scraperName));

      verify(scraperConfigRepository).exists(scraperName);
      verify(scraperConfigRepository, never()).create(scraperName, true);
      verify(scraperConfigRepository, never()).deactivate(scraperName);
    }

    @Test
    void whenDeactivateExistsScraperThanFinishSuccessfully() {
      //given
      final String scraperName = "sc3";
      //when
      when(scraperConfigRepository.exists(scraperName)).thenReturn(true);
      //then
      assertDoesNotThrow(() -> scraperFacade.deactivateScraperByConfigurationName(scraperName));

      verify(scraperConfigRepository).exists(scraperName);
      verify(scraperConfigRepository, never()).create(scraperName, true);
      verify(scraperConfigRepository).deactivate(scraperName);
    }
  }

  @Nested
  class LastScrapedEventMarkerTest {
    @Test
    void whenFoundLastScrapedEventMarkerThenReturnNotEmptyOptional() {
      //given
      final String scraperName = "scm4";
      final LastScrapedEventMarker lastScrapedEventMarker = new LastScrapedEventMarker(scraperName, LocalDateTime.now().minusDays(2), "Event Title", "Marker", false);
      //when
      when(lastScrapedEventMarkerRepository.findByScraperConfigurationName(scraperName, true))
          .thenReturn(Optional.of(lastScrapedEventMarker));
      //then

      assertDoesNotThrow(() -> {
        final Optional<LastScrapedEventMarker> lastScrapedEventMarkerResult = scraperFacade.lastScrapedEventMarkerByConfigurationName(scraperName);
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
      final String scraperName = "scm5";
      //when
      when(lastScrapedEventMarkerRepository.findByScraperConfigurationName(scraperName, true))
          .thenReturn(Optional.empty());
      //then

      assertDoesNotThrow(() -> {
        final Optional<LastScrapedEventMarker> lastScrapedEventMarkerResult = scraperFacade.lastScrapedEventMarkerByConfigurationName(scraperName);
        assertThat(lastScrapedEventMarkerResult)
            .isEmpty();
      });

      verify(lastScrapedEventMarkerRepository).findByScraperConfigurationName(scraperName, true);
    }

    @Test
    void whenCreateLastScrapedEventMarkerThenReplaceNotCompleteOne() {
      //given
      final String scraperConfigurationName = "name1";
      final LocalDateTime runTime = LocalDateTime.now();

      final LastScrapedEventMarker previousLastScrapedEventMarker = new LastScrapedEventMarker(scraperConfigurationName, runTime.minusDays(1), "Title 1", "Marker", false);
      final LastScrapedEventMarker newLastScrapedEventMarker = new LastScrapedEventMarker(scraperConfigurationName, runTime, "Title 2", "Marker 2", false);

      //when
      when(lastScrapedEventMarkerRepository.findByScraperConfigurationName(scraperConfigurationName, false))
          .thenReturn(Optional.of(previousLastScrapedEventMarker));

      //then
      scraperFacade.saveLastScrapedEventMarker(scraperConfigurationName, runTime, newLastScrapedEventMarker.eventTitle(), newLastScrapedEventMarker.marker());

      verify(lastScrapedEventMarkerRepository).findByScraperConfigurationName(scraperConfigurationName, false);
      verify(lastScrapedEventMarkerRepository).drop(previousLastScrapedEventMarker);

      verify(lastScrapedEventMarkerRepository).store(lastScrapedEventMarkerArgumentCaptor.capture());

      assertThat(lastScrapedEventMarkerArgumentCaptor.getValue())
          .isEqualTo(newLastScrapedEventMarker);

    }
  }
}
package hub.event.scrapers.core;

import hub.event.scrapers.core.exceptions.ScraperConfigurationByNameNotExists;
import hub.event.scrapers.core.scraper.LastScrapedEventMarker;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZoneId;

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
      verify(scraperConfigRepository).create(scraperName, ZoneId.systemDefault(), true);
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
      verify(scraperConfigRepository, never()).create(scraperName, ZoneId.systemDefault(), true);
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
      verify(scraperConfigRepository, never()).create(scraperName, ZoneId.systemDefault(), true);
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
      verify(scraperConfigRepository, never()).create(scraperName, ZoneId.systemDefault(), true);
      verify(scraperConfigRepository).deactivate(scraperName);
    }
  }

}
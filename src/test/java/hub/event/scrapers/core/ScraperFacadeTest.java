package hub.event.scrapers.core;

import hub.event.scrapers.core.exceptions.ScraperConfigurationByNameNotExists;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
  private ScraperConfigRepository scraperConfigRepository;
  @Mock
  private ScraperIdNameCache scraperIdNameCache;
  @InjectMocks
  private ScraperFacade scraperFacade;

  @Nested
  class ScraperConfigTest {
    @Test
    void whenActivateScraperThatNotExistThenThrowException() {
      //given
      final String scraperName = "sc1";
      //when
      int scraperId = 100;
      when(scraperIdNameCache.getIdByScraperName(scraperName)).thenReturn(scraperId);
      when(scraperConfigRepository.exists(scraperId)).thenReturn(false);
      //then
      Assertions.assertThatExceptionOfType(ScraperConfigurationByNameNotExists.class)
          .isThrownBy(() -> scraperFacade.activateScraperByConfigurationName(scraperName));

      verify(scraperConfigRepository).exists(scraperId);
      verify(scraperConfigRepository, never()).activate(scraperId);
    }

    @Test
    void whenActivateScraperThatExistThenCallActive() {
      //given
      final String scraperName = "sc4";
      //when
      int scraperId = 4;
      when(scraperIdNameCache.getIdByScraperName(scraperName)).thenReturn(scraperId);
      when(scraperConfigRepository.exists(scraperId)).thenReturn(true);
      //then
      Assertions.assertThatCode(() -> scraperFacade.activateScraperByConfigurationName(scraperName))
          .doesNotThrowAnyException();

      verify(scraperConfigRepository).exists(scraperId);
      verify(scraperConfigRepository, never()).create(scraperName, ZoneId.systemDefault(), true);
      verify(scraperConfigRepository).activate(scraperId);
    }

    @Test
    void whenDeactivateNotExistsScraperThanThrowConfigNotExistsException() {
      //given
      final String scraperName = "sc2";
      //when
      int scraperId = 2;
      when(scraperIdNameCache.getIdByScraperName(scraperName)).thenReturn(scraperId);
      when(scraperConfigRepository.exists(scraperId)).thenReturn(false);
      //then
      assertThrows(ScraperConfigurationByNameNotExists.class, () -> scraperFacade.deactivateScraperByConfigurationName(scraperName));

      verify(scraperConfigRepository).exists(scraperId);
      verify(scraperConfigRepository, never()).deactivate(scraperId);
    }

    @Test
    void whenDeactivateExistsScraperThenCallDeactivate() {
      //given
      final String scraperName = "sc3";
      //when
      int scraperId = 3;
      when(scraperIdNameCache.getIdByScraperName(scraperName)).thenReturn(scraperId);
      when(scraperConfigRepository.exists(scraperId)).thenReturn(true);
      //then
      assertDoesNotThrow(() -> scraperFacade.deactivateScraperByConfigurationName(scraperName));

      verify(scraperConfigRepository).exists(scraperId);
      verify(scraperConfigRepository).deactivate(scraperId);
    }
  }

}
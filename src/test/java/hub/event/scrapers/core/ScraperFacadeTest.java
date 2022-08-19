package hub.event.scrapers.core;

import hub.event.scrapers.core.exceptions.ScraperConfigurationByNameNotExists;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ScraperFacadeTest {
  @Mock
  private LastScrapedEventMarkerRepository lastScrapedEventMarkerRepository;
  @Mock
  private ScraperConfigRepository scraperConfigRepository;
  @InjectMocks
  private ScraperFacade scraperFacade;

  @Test
  void whenActivateScraperThanNotExistThenCreateNewRecord() {
    //given
    final String scraperName = "sc1";
    //when
    when(scraperConfigRepository.exists(scraperName)).thenReturn(false);
    //then
    scraperFacade.activateScraperByConfigurationName(scraperName);

    verify(scraperConfigRepository).exists(scraperName);
    verify(scraperConfigRepository).create(scraperName);
    verify(scraperConfigRepository, never()).activate(scraperName);
  }

  @Test
  void whenActivateScraperThanExistThenFinishSuccessfully() {
    //given
    final String scraperName = "sc4";
    //when
    when(scraperConfigRepository.exists(scraperName)).thenReturn(true);
    //then
    scraperFacade.activateScraperByConfigurationName(scraperName);

    verify(scraperConfigRepository).exists(scraperName);
    verify(scraperConfigRepository, never()).create(scraperName);
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
    verify(scraperConfigRepository, never()).create(scraperName);
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
    verify(scraperConfigRepository, never()).create(scraperName);
    verify(scraperConfigRepository).deactivate(scraperName);
  }
}
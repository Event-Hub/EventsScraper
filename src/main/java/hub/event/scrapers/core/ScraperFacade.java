package hub.event.scrapers.core;

import hub.event.scrapers.core.exceptions.ScraperConfigurationByNameNotExists;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ScraperFacade {
  private final LastScrapedEventMarkerRepository lastScrapedEventMarkerRepository;
  private final ScraperConfigRepository scraperConfigRepository;

  private final DuplicatedEventCandidateRepository duplicatedEventCandidateRepository;

  public ScraperFacade(LastScrapedEventMarkerRepository lastScrapedEventMarkerRepository, ScraperConfigRepository scraperConfigRepository, DuplicatedEventCandidateRepository duplicatedEventCandidateRepository) {
    this.lastScrapedEventMarkerRepository = lastScrapedEventMarkerRepository;
    this.scraperConfigRepository = scraperConfigRepository;
    this.duplicatedEventCandidateRepository = duplicatedEventCandidateRepository;
  }

  public void activateScraperByConfigurationName(String scraperName) {
    if (scraperConfigRepository.exists(scraperName)) {
      scraperConfigRepository.activate(scraperName);
    } else {
      scraperConfigRepository.create(scraperName, true);
    }
  }

  public void deactivateScraperByConfigurationName(String scraperName) throws ScraperConfigurationByNameNotExists {
    if (!scraperConfigRepository.exists(scraperName)) {
      throw new ScraperConfigurationByNameNotExists(scraperName);
    }

    scraperConfigRepository.deactivate(scraperName);
  }


  public Optional<LastScrapedEventMarker> lastScrapedEventMarkerByConfigurationName(String scraperConfigurationName) {
    return lastScrapedEventMarkerRepository.findByScraperConfigurationName(scraperConfigurationName, true);
  }

  public void saveLastScrapedEventMarker(String scraperConfigurationName, LocalDateTime runDateTime, String eventTitle, String marker) {
    final Optional<LastScrapedEventMarker> savedScrapedEventMarker = lastScrapedEventMarkerRepository.findByScraperConfigurationName(scraperConfigurationName, false);
    savedScrapedEventMarker.ifPresent(lastScrapedEventMarkerRepository::drop);

    final LastScrapedEventMarker newScrapedEventMarkerToSave = new LastScrapedEventMarker(scraperConfigurationName, runDateTime, eventTitle, marker);
    lastScrapedEventMarkerRepository.store(newScrapedEventMarkerToSave);
  }

  public List<DuplicatedEventCandidate> getAllDuplicatedEventCandidates() {
    return duplicatedEventCandidateRepository.getAllDuplicatedEventCandidates();
  }

  public boolean deleteDuplicatedEventCandidateByUuid(UUID eventCandidateUuid) {
    return duplicatedEventCandidateRepository.deleteDuplicatedEventCandidateByUuid(eventCandidateUuid);
  }
}

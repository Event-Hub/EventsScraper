package hub.event.scrapers.core;

import org.apache.commons.lang3.NotImplementedException;
import org.springframework.stereotype.Service;

import java.util.List;
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
  }

  public void deactivateScraperByConfigurationName(String scraperName) {
  }

  //Maybe should be in separated module
  public List<DuplicatedEventCandidate> getAllProbablyDuplicatedEventCandidates(){
    throw new NotImplementedException("getAllProbablyDuplicatedEventCandidates is not implemented. ");
  }

  //Maybe should be in separated module
  public boolean deleteByProbablyDuplicatedEventCandidateUuid(UUID eventCandidateUuid){
    throw new NotImplementedException("deleteByProbablyDuplicatedEventCandidateUuid is not implemented. ");
  }

}

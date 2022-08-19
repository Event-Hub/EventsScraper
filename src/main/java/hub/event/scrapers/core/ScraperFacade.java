package hub.event.scrapers.core;

import org.apache.commons.lang3.NotImplementedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ScraperFacade {
private final LastScrapedEventMarkerRepository lastScrapedEventMarkerRepository;
private final ScraperConfigRepository scraperConfigRepository;

private final ProbablyDuplicatedEventCandidateRepository probablyDuplicatedEventCandidateRepository;

  public ScraperFacade(LastScrapedEventMarkerRepository lastScrapedEventMarkerRepository, ScraperConfigRepository scraperConfigRepository, ProbablyDuplicatedEventCandidateRepository probablyDuplicatedEventCandidateRepository) {
    this.lastScrapedEventMarkerRepository = lastScrapedEventMarkerRepository;
    this.scraperConfigRepository = scraperConfigRepository;
    this.probablyDuplicatedEventCandidateRepository = probablyDuplicatedEventCandidateRepository;
  }

  public void activateScraperByConfigurationName(String scraperName) {
  }

  public void deactivateScraperByConfigurationName(String scraperName) {
  }

  //Maybe should be in separated module
  public List<ProbablyDuplicatedEventCandidate> getAllProbablyDuplicatedEventCandidates(){
    throw new NotImplementedException("getAllProbablyDuplicatedEventCandidates is not implemented. ");
  }

  //Maybe should be in separated module
  public boolean deleteByProbablyDuplicatedEventCandidateUuid(UUID eventCandidateUuid){
    throw new NotImplementedException("deleteByProbablyDuplicatedEventCandidateUuid is not implemented. ");
  }

}

package hub.event.scrapers.core;

import java.util.List;

interface ProbablyDuplicatedEventCandidateRepository {
  void saveAll(List<ProbablyDuplicatedEventCandidate> candidates);
}

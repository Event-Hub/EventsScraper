package hub.event.scrapers.core;

import java.util.List;

interface DuplicatedEventCandidateRepository {
  void saveAll(List<DuplicatedEventCandidate> candidates);
}

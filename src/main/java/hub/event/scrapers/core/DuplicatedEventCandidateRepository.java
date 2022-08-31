package hub.event.scrapers.core;

import java.util.List;
import java.util.UUID;

interface DuplicatedEventCandidateRepository {
  void saveAll(List<DuplicatedEventCandidate> candidates);

  List<DuplicatedEventCandidate> getAllDuplicatedEventCandidates();

  boolean deleteDuplicatedEventCandidateByUuid(UUID eventCandidateUuid);
}

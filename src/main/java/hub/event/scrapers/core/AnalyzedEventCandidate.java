package hub.event.scrapers.core;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

class AnalyzedEventCandidate {
  private final List<ScrapedEvent> duplicateWithCandidates;
  private final List<ExistsEvent> duplicateWithEvents;
  private final ScrapedEvent scrapedEvent;

  AnalyzedEventCandidate(ScrapedEvent scrapedEvent) {
    this.scrapedEvent = scrapedEvent;
    this.duplicateWithCandidates = new ArrayList<>();
    this.duplicateWithEvents = new ArrayList<>();
  }

  ScrapedEvent scrapedEvent() {
    return scrapedEvent;
  }

  void addDuplicateCandidate(ScrapedEvent scrapedEvent) {
    duplicateWithCandidates.add(scrapedEvent);
  }

  void addDuplicateEvent(ExistsEvent existsEvent) {
    duplicateWithEvents.add(existsEvent);
  }

  boolean isDuplicate() {
    return !duplicateWithCandidates.isEmpty() || !duplicateWithEvents.isEmpty();
  }

  boolean isNotDuplicate() {
    return duplicateWithCandidates.isEmpty() && duplicateWithEvents.isEmpty();
  }

  public List<UUID> duplicateCandidateUUIDsList() {
    return duplicateWithCandidates.stream()
        .map(ScrapedEvent::uuid)
        .toList();
  }

  public List<Integer> duplicateEventIdList() {
    return duplicateWithEvents.stream()
        .map(ExistsEvent::eventId)
        .toList();
  }
}

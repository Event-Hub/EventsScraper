package hub.event.scrapers.core;

import java.util.List;
import java.util.UUID;

class ProbablyDuplicatedEventCandidate {
  private UUID uuid;
  private ScrapedEvent scrapedEvent;
  private List<UUID> duplicatedCandidates;
  private List<ExistsEvent> duplicatedEvents;

  UUID getUuid() {
    return uuid;
  }

  ScrapedEvent getScrapedEvent() {
    return scrapedEvent;
  }

  List<UUID> getDuplicatedCandidates() {
    return duplicatedCandidates;
  }

  List<ExistsEvent> getDuplicatedEvents() {
    return duplicatedEvents;
  }
}

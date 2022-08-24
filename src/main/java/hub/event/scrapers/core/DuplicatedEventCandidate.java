package hub.event.scrapers.core;

import java.util.List;
import java.util.UUID;

class DuplicatedEventCandidate {
  private ScrapedEvent scrapedEvent;
  private List<UUID> duplicatedCandidates;
  private List<Integer> duplicatedEvents;

  public DuplicatedEventCandidate(ScrapedEvent scrapedEvent, List<UUID> duplicatedCandidates, List<Integer> duplicatedEvents) {
    this.scrapedEvent = scrapedEvent;
    this.duplicatedCandidates = duplicatedCandidates;
    this.duplicatedEvents = duplicatedEvents;
  }


  ScrapedEvent getScrapedEvent() {
    return scrapedEvent;
  }

  List<UUID> getDuplicatedCandidates() {
    return duplicatedCandidates;
  }

  List<Integer> getDuplicatedEvents() {
    return duplicatedEvents;
  }
}

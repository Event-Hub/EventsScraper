package hub.event.scrapers.core;

import java.util.ArrayList;
import java.util.List;

class AnalyzedEventCandidate {
  private final List<ScrapedEvent> probablyDuplicateCandidates;
  private final List<ExistsEvent> probablyDuplicateEvents;
  private final ScrapedEvent scrapedEvent;

  AnalyzedEventCandidate(ScrapedEvent scrapedEvent) {
    this.scrapedEvent = scrapedEvent;
    this.probablyDuplicateCandidates = new ArrayList<>();
    this.probablyDuplicateEvents = new ArrayList<>();
  }

  boolean isDuplicateWithCandidate() {
    return !this.probablyDuplicateCandidates.isEmpty();
  }

  void addDuplicateCandidate(ScrapedEvent scrapedEvent) {
    this.probablyDuplicateCandidates.add(scrapedEvent);
  }

  boolean isDuplicateWithEvent() {
    return !this.probablyDuplicateEvents.isEmpty();
  }

  void addDuplicateEvent(ExistsEvent existsEvent) {
    this.probablyDuplicateEvents.add(existsEvent);
  }
}

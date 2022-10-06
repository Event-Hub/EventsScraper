package hub.event.scrapers.core;

import java.util.List;

class EventCandidateAnalyzer {
  List<AnalyzedEventCandidate> analyze(List<ScrapedEvent> scrapedEventList) {
    // nie idź do event modułu żeby zdecydować o zapisie lub pominięciu.
    // niech event moduł sam zdecyduje co mu się podoba
    //TODO replace this just map implementation by real one
    return scrapedEventList.stream()
        .map(this::mapAnalyzedEventCandidate)
        .toList();
  }

  private AnalyzedEventCandidate mapAnalyzedEventCandidate(ScrapedEvent scrapedEvent) {
    return new AnalyzedEventCandidate(scrapedEvent);
  }
}

package hub.event.scrapers.core;

import java.util.List;

class EventCandidateAnalyzer {
  List<AnalyzedEventCandidate> analyze(List<ScrapedEvent> scrapedEventList) {
    //TODO replace this just map implementation by real one
    return scrapedEventList.stream()
        .map(this::mapAnalyzedEventCandidate)
        .toList();
  }

  private AnalyzedEventCandidate mapAnalyzedEventCandidate(ScrapedEvent scrapedEvent) {
    return new AnalyzedEventCandidate(scrapedEvent);
  }
}

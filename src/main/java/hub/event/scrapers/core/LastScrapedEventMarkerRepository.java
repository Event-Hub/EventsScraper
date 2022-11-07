package hub.event.scrapers.core;

import hub.event.scrapers.core.scraper.LastScrapedEventMarker;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Repository
class LastScrapedEventMarkerRepository {
  private final JpaLastScrapedEventMarkerRepository jpaLastScrapedEventMarkerRepository;
  private final ScraperIdNameCache scraperIdNameCache;

  LastScrapedEventMarkerRepository(JpaLastScrapedEventMarkerRepository lastScrapedEventMarkerEntityRepository, ScraperIdNameCache scraperIdNameCache) {
    this.jpaLastScrapedEventMarkerRepository = lastScrapedEventMarkerEntityRepository;
    this.scraperIdNameCache = scraperIdNameCache;
  }

  void store(LastScrapedEventMarker lastScrapedEventMarker) {
    final EntityLastScrapedEventMarker entityLastScrapedEventMarker = mapToEntity(lastScrapedEventMarker);
    jpaLastScrapedEventMarkerRepository.save(entityLastScrapedEventMarker);
  }

  void setAllAsCompleteByConfigurationsIds(List<Integer> ids) {
    jpaLastScrapedEventMarkerRepository.setAllAsCompleteByConfigurationsIds(ids, ZonedDateTime.now().minusDays(1).toInstant());
  }

  Optional<LastScrapedEventMarker> findLastCompletedByScraperConfigurationId(Integer scraperId) {
    final Optional<EntityLastScrapedEventMarker> scraperConfigurationName = jpaLastScrapedEventMarkerRepository.findByScraperId(scraperId);
    return scraperConfigurationName.map(this::mapToMaker);
  }

  private EntityLastScrapedEventMarker mapToEntity(LastScrapedEventMarker lastScrapedEventMarker) {
    final Integer scraperId = scraperIdNameCache.getIdByScraperName(lastScrapedEventMarker.scraperConfigurationName());
    final EntityLastScrapedEventMarker entityLastScrapedEventMarker = new EntityLastScrapedEventMarker();

    entityLastScrapedEventMarker.setMarker(lastScrapedEventMarker.marker());
    entityLastScrapedEventMarker.setEventTitle(lastScrapedEventMarker.eventTitle());
    entityLastScrapedEventMarker.setRunTime(lastScrapedEventMarker.runDateTime());
    entityLastScrapedEventMarker.setScraperId(scraperId);
    entityLastScrapedEventMarker.setComplete(lastScrapedEventMarker.complete());

    return entityLastScrapedEventMarker;
  }

  private LastScrapedEventMarker mapToMaker(EntityLastScrapedEventMarker entityLastScrapedEventMarker) {
    final String marker = entityLastScrapedEventMarker.getMarker();
    final Instant date = entityLastScrapedEventMarker.getRunTime();
    final String title = entityLastScrapedEventMarker.getEventTitle();
    final String configurationName = scraperIdNameCache.getScraperNameById(entityLastScrapedEventMarker.getScraperId());
    final Boolean isComplete = entityLastScrapedEventMarker.getComplete();

    return new LastScrapedEventMarker(configurationName, date, title, marker, isComplete);
  }

}

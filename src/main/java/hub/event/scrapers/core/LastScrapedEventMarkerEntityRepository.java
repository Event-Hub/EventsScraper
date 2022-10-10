package hub.event.scrapers.core;

import hub.event.scrapers.core.scraper.LastScrapedEventMarker;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
class LastScrapedEventMarkerEntityRepository implements LastScrapedEventMarkerRepository {
  private final LastScrapedEventMarkerJpaRepository lastScrapedEventMarkerJpaRepository;

  LastScrapedEventMarkerEntityRepository(LastScrapedEventMarkerJpaRepository lastScrapedEventMarkerEntityRepository) {
    this.lastScrapedEventMarkerJpaRepository = lastScrapedEventMarkerEntityRepository;
  }

  public void store(LastScrapedEventMarker lastScrapedEventMarker) {
    final LastScrapedEventMarkerEntity lastScrapedEventMarkerEntity = mapToEntity(lastScrapedEventMarker);
    lastScrapedEventMarkerJpaRepository.save(lastScrapedEventMarkerEntity);
  }

  @Override
  public void drop(LastScrapedEventMarker lastScrapedEventMarker) {
    lastScrapedEventMarkerJpaRepository.deleteById(lastScrapedEventMarker.scraperConfigurationName());
  }

  @Override
  public void makeDraftActive(List<String> configurationNameList) {
    lastScrapedEventMarkerJpaRepository.updateSetAllActiveById(configurationNameList);
  }

  public Optional<LastScrapedEventMarker> findByScraperConfigurationName(String configurationName, boolean complete) {
    final Optional<LastScrapedEventMarkerEntity> scraperConfigurationName = lastScrapedEventMarkerJpaRepository.findByScraperConfigurationName(configurationName);
    return scraperConfigurationName.map(this::mapToMaker);
  }

  private LastScrapedEventMarkerEntity mapToEntity(LastScrapedEventMarker lastScrapedEventMarker) {
    final LastScrapedEventMarkerEntity lastScrapedEventMarkerEntity = new LastScrapedEventMarkerEntity();
    lastScrapedEventMarkerEntity.setMarker(lastScrapedEventMarker.marker());
    lastScrapedEventMarkerEntity.setEventTitle(lastScrapedEventMarker.eventTitle());
    lastScrapedEventMarkerEntity.setRunDateTime(lastScrapedEventMarker.runDateTime());
    lastScrapedEventMarkerEntity.setScraperConfigurationName(lastScrapedEventMarker.scraperConfigurationName());
    return lastScrapedEventMarkerEntity;
  }

  private LastScrapedEventMarker mapToMaker(LastScrapedEventMarkerEntity lastScrapedEventMarkerEntity) {
    final String marker = lastScrapedEventMarkerEntity.getMarker();
    final Instant date = lastScrapedEventMarkerEntity.getRunDateTime();
    final String title = lastScrapedEventMarkerEntity.getEventTitle();
    final String configurationName = lastScrapedEventMarkerEntity.getScraperConfigurationName();
    final Boolean isComplete = lastScrapedEventMarkerEntity.getComplete();

    return new LastScrapedEventMarker(configurationName, date, title, marker, isComplete);
  }

}

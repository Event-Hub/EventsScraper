package hub.event.scrapers.core.entityrepository;

import hub.event.scrapers.core.LastScrapedEventMarker;
import hub.event.scrapers.core.LastScrapedEventMarkerRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
class LastScrapedEventMarkerEntityRepository implements LastScrapedEventMarkerRepository {
  private final LastScrapedEventMarkerJpaRepository lastScrapedEventMarkerEntityRepository;

  LastScrapedEventMarkerEntityRepository(LastScrapedEventMarkerJpaRepository lastScrapedEventMarkerEntityRepository) {
    this.lastScrapedEventMarkerEntityRepository = lastScrapedEventMarkerEntityRepository;
  }

  public void store(LastScrapedEventMarker lastScrapedEventMarker) {
    final LastScrapedEventMarkerEntity lastScrapedEventMarkerEntity = mapToEntity(lastScrapedEventMarker);
    lastScrapedEventMarkerEntityRepository.save(lastScrapedEventMarkerEntity);
  }

  public Optional<LastScrapedEventMarker> findByScraperConfigurationName(String configurationName) {
    final Optional<LastScrapedEventMarkerEntity> scraperConfigurationName = lastScrapedEventMarkerEntityRepository.findByScraperConfigurationName(configurationName);
    return scraperConfigurationName.map(this::mapToMaker);
  }

  private LastScrapedEventMarkerEntity mapToEntity(LastScrapedEventMarker lastScrapedEventMarker) {
    final LastScrapedEventMarkerEntity lastScrapedEventMarkerEntity = new LastScrapedEventMarkerEntity();
    lastScrapedEventMarkerEntity.setMarker(lastScrapedEventMarker.marker());
    lastScrapedEventMarkerEntity.setEventTitle(lastScrapedEventMarker.eventTitle());
    lastScrapedEventMarkerEntity.setEventDate(lastScrapedEventMarker.eventDate());
    lastScrapedEventMarkerEntity.setScraperConfigurationName(lastScrapedEventMarker.scraperConfigurationName());
    return lastScrapedEventMarkerEntity;
  }

  private LastScrapedEventMarker mapToMaker(LastScrapedEventMarkerEntity lastScrapedEventMarkerEntity) {
    final String marker = lastScrapedEventMarkerEntity.getMarker();
    final LocalDateTime date = lastScrapedEventMarkerEntity.getEventDate();
    final String title = lastScrapedEventMarkerEntity.getEventTitle();
    final String configurationName = lastScrapedEventMarkerEntity.getScraperConfigurationName();

    return new LastScrapedEventMarker(configurationName, date, title, marker);
  }
}

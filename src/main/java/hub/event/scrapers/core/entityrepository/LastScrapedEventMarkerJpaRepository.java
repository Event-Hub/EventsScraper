package hub.event.scrapers.core.entityrepository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

interface LastScrapedEventMarkerJpaRepository extends JpaRepository<LastScrapedEventMarkerEntity, String> {

  Optional<LastScrapedEventMarkerEntity> findByScraperConfigurationName(String configurationName);
}

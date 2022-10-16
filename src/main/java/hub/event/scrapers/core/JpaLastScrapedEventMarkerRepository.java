package hub.event.scrapers.core;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

interface JpaLastScrapedEventMarkerRepository extends JpaRepository<EntityLastScrapedEventMarker, Integer> {

  Optional<EntityLastScrapedEventMarker> findByScraperId(Integer scraperId);

  @Modifying
  @Query("UPDATE scraper_scraped_event_maker m SET m.isComplete = true WHERE m.isComplete = false AND m.runTime >= :date AND  m.scraperId IN :configurationIds")
  @Transactional
  void updateSetAllActiveById(@Param("configurationIds") List<Integer> configurationIds, @Param("date")Instant date);
}

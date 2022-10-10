package hub.event.scrapers.core;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

interface LastScrapedEventMarkerJpaRepository extends JpaRepository<LastScrapedEventMarkerEntity, String> {

  Optional<LastScrapedEventMarkerEntity> findByScraperConfigurationName(String configurationName);

  @Modifying
  @Query("update LastScrapedEventMarkerEntity e set e.complete=true where e.scraperConfigurationName in :ids")
  void updateSetAllActiveById(@Param("ids") List<String> configurationNameList);
}

package hub.event.scrapers.core;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

interface JpaScraperConfigRepository extends JpaRepository<EntityScraperConfig, Integer> {

  @Modifying
  @Transactional
  @Query("update scraper_config e set e.isActive = :state where e.scraperId = :id")
  void setActiveState(@Param("id") Integer scraperId, @Param("state") boolean activeState);

}

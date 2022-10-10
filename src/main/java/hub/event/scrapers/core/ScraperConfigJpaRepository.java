package hub.event.scrapers.core;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

interface ScraperConfigJpaRepository extends JpaRepository<ScraperConfigEntity, String> {

  @Modifying
  @Query("update ScraperConfigEntity e set e.activeState = :state where e.scraperName = :name" )
  void setActiveState(@Param("name") String scraperConfigurationName,  @Param("state") boolean activeState);

}

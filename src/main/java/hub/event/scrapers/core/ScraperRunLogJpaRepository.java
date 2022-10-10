package hub.event.scrapers.core;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

interface ScraperRunLogJpaRepository extends JpaRepository<ScraperRunStatusLogEntity, String>, JpaSpecificationExecutor<ScraperRunStatusLogEntity> {
}

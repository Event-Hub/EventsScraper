package hub.event.scrapers.core;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

interface JpaScraperRunLogRepository extends JpaRepository<EntityScraperRunStatusLog, Integer>, JpaSpecificationExecutor<EntityScraperRunStatusLog> {
}

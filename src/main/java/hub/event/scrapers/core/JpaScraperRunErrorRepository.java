package hub.event.scrapers.core;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

interface JpaScraperRunErrorRepository extends JpaRepository<EntityScraperRunErrorLog, Integer>, JpaSpecificationExecutor<EntityScraperRunErrorLog> {
}

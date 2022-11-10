package hub.event.scrapers.core.runlog;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

interface JpaScraperRunErrorQueryRepository extends JpaRepository<EntityScraperRunErrorLog, Integer>, JpaSpecificationExecutor<EntityScraperRunErrorLog> {
}

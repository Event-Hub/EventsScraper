package hub.event.scrapers.core;

import org.springframework.data.jpa.repository.JpaRepository;

interface ScraperRunErrorJpaRepository extends JpaRepository<ScraperRunErrorLogEntity, String> {
}

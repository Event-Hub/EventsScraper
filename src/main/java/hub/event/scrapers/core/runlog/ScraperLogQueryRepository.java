package hub.event.scrapers.core.runlog;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

@Repository
class ScraperLogQueryRepository {
  private final JpaScraperRunLogQueryRepository runLogQueryRepository;
  private final JpaScraperRunErrorQueryRepository runErrorQueryRepository;

  ScraperLogQueryRepository(JpaScraperRunLogQueryRepository runLogQueryRepository, JpaScraperRunErrorQueryRepository runErrorQueryRepository) {
    this.runLogQueryRepository = runLogQueryRepository;
    this.runErrorQueryRepository = runErrorQueryRepository;
  }

  List<ScraperRunErrorLog> findAllErrorLog(ErrorLogSearchQuery errorLogSearchQuery) {
    final Sort sort = Sort.by(
        Sort.Order.asc(EntityScraperRunErrorLogProperties.SCRAPER_CONFIGURATION_NAME_PATH),
        Sort.Order.desc(EntityScraperRunErrorLogProperties.TIME),
        Sort.Order.desc(EntityScraperRunErrorLogProperties.ID)
    );

    final Specification<EntityScraperRunErrorLog> findAllSpecification = new EntityFindAllErrorLogSpecification(errorLogSearchQuery);
    final Pageable pageable = extractPageSettings(errorLogSearchQuery, sort);

    return findAllErrorLog(findAllSpecification, pageable , sort)
        .stream()
        .map(this::mapToLog)
        .toList();

  }

  List<ScraperRunStatusLog> findAllStatusLog(StatusLogSearchQuery statusLogSearchQuery) {
    final Sort sort = Sort.by(
        Sort.Order.asc(EntityScraperRunStatusLogProperties.SCRAPER_CONFIGURATION_NAME_PATH),
        Sort.Order.desc(EntityScraperRunStatusLogProperties.START_TIME),
        Sort.Order.desc(EntityScraperRunStatusLogProperties.ID)
    );
    final Specification<EntityScraperRunStatusLog> findAllSpecification = new EntityFindAllStatusLogSpecification(statusLogSearchQuery);
    final Pageable pageable = extractPageSettings(statusLogSearchQuery, sort);

    return findAllStatusLog(findAllSpecification, pageable, sort)
        .stream()
        .map(this::mapToLog)
        .toList();
  }

  private List<EntityScraperRunStatusLog> findAllStatusLog(Specification<EntityScraperRunStatusLog> findAllSpecification, Pageable pageable, Sort sort) {
    if (Objects.isNull(pageable)) {
      return runLogQueryRepository.findAll(findAllSpecification, sort);
    }
    return runLogQueryRepository.findAll(findAllSpecification, pageable).toList();
  }

  private List<EntityScraperRunErrorLog> findAllErrorLog(Specification<EntityScraperRunErrorLog> findAllSpecification, Pageable pageable, Sort sort) {
    if (Objects.isNull(pageable)) {
      return runErrorQueryRepository.findAll(findAllSpecification, sort);
    }
    return runErrorQueryRepository.findAll(findAllSpecification, pageable).toList();
  }

  private ScraperRunStatusLog mapToLog(EntityScraperRunStatusLog entityScraperRunStatusLog) {

    return new ScraperRunStatusLog(
        entityScraperRunStatusLog.getScraperConfigurationName(),
        entityScraperRunStatusLog.getStartTime(),
        entityScraperRunStatusLog.getFinishTime(),
        entityScraperRunStatusLog.getScannedEventCount(),
        entityScraperRunStatusLog.getErrorCount()
    );
  }

  private ScraperRunErrorLog mapToLog(EntityScraperRunErrorLog entityScraperRunErrorLog) {

    return new ScraperRunErrorLog(
        entityScraperRunErrorLog.getScraperConfigurationName(),
        entityScraperRunErrorLog.getTime(),
        entityScraperRunErrorLog.getErrorCode(),
        entityScraperRunErrorLog.getDescription());
  }

  private Pageable extractPageSettings(StatusLogSearchQuery statusLogSearchQuery, Sort sort) {
    return statusLogSearchQuery.hasPageSetting()
        ? PageRequest.of(statusLogSearchQuery.page(), statusLogSearchQuery.pageSize(),sort )
        : null;
  }

  private Pageable extractPageSettings(ErrorLogSearchQuery errorLogSearchQuery, Sort sort) {
    return errorLogSearchQuery.hasPageSetting()
        ? PageRequest.of(errorLogSearchQuery.page(), errorLogSearchQuery.pageSize(), sort)
        : null;
  }

}

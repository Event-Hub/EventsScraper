package hub.event.scrapers.core;

import hub.event.scrapers.core.runlog.ErrorLogSearchQuery;
import hub.event.scrapers.core.runlog.ScraperRunErrorLog;
import hub.event.scrapers.core.runlog.ScraperRunStatusLog;
import hub.event.scrapers.core.runlog.StatusLogSearchQuery;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
class ScraperRunLogRepository {

  private ScraperRunLogJpaRepository scraperRunLogJpaRepository;
  private ScraperRunErrorJpaRepository scraperRunErrorJpaRepository;

  void save(ScraperRunStatusLog scraperRunStatusLog) {
    ScraperRunStatusLogEntity scraperRunStatusLogEntity = mapToEntity(scraperRunStatusLog);
    scraperRunLogJpaRepository.save(scraperRunStatusLogEntity);
  }

  void save(ScraperRunErrorLog scraperRunError) {
    ScraperRunErrorLogEntity scraperRunErrorLogEntity = mapToEntity(scraperRunError);
    scraperRunErrorJpaRepository.save(scraperRunErrorLogEntity);
  }

  List<ScraperRunErrorLog> findAllErrorLog(ErrorLogSearchQuery errorLogSearchQuery) {
    final Sort sort = Sort.by(Sort.Direction.ASC, "configurationName");
    final Specification<ScraperRunErrorLogEntity> findAllSpecification = new FindAllErrorLogSpecification(errorLogSearchQuery);
    final Pageable pageable = extractPageSettings(errorLogSearchQuery, sort);

    return findAllErrorLog(findAllSpecification, pageable, sort)
        .stream()
        .map(this::mapToLog)
        .toList();

  }

  List<ScraperRunStatusLog> findAllStatusLog(StatusLogSearchQuery statusLogSearchQuery) {
    final Sort sort = Sort.by(Sort.Direction.ASC, "configurationName");
    final Specification<ScraperRunStatusLogEntity> findAllSpecification = new FindAllStatusLogSpecification(statusLogSearchQuery);
    final Pageable pageable = extractPageSettings(statusLogSearchQuery, sort);

    return findAllStatusLog(findAllSpecification, pageable, sort)
        .stream()
        .map(this::mapToLog)
        .toList();
  }

  private List<ScraperRunStatusLogEntity> findAllStatusLog(Specification<ScraperRunStatusLogEntity> findAllSpecification, Pageable pageable, Sort sort) {
    if (Objects.isNull(pageable)) {
      return scraperRunLogJpaRepository.findAll(findAllSpecification, sort);
    }
    return scraperRunLogJpaRepository.findAll(findAllSpecification, pageable).toList();
  }

  private List<ScraperRunErrorLogEntity> findAllErrorLog(Specification<ScraperRunErrorLogEntity> findAllSpecification, Pageable pageable, Sort sort) {
    return null;
  }

  private ScraperRunStatusLog mapToLog(ScraperRunStatusLogEntity scraperRunStatusLogEntity) {
    return new ScraperRunStatusLog(
        scraperRunStatusLogEntity.getConfigurationName(),
        scraperRunStatusLogEntity.getStartTime(),
        scraperRunStatusLogEntity.getFinishTime(),
        scraperRunStatusLogEntity.getScannedEventCount(),
        scraperRunStatusLogEntity.getErrorCount()
    );
  }

  private ScraperRunErrorLog mapToLog(ScraperRunErrorLogEntity scraperRunErrorLogEntity) {
    return null;
  }

  private ScraperRunStatusLogEntity mapToEntity(ScraperRunStatusLog scraperRunStatusLog) {
    return new ScraperRunStatusLogEntity(
        scraperRunStatusLog.configurationName(),
        scraperRunStatusLog.startTime(),
        scraperRunStatusLog.finishTime(),
        scraperRunStatusLog.scannedEventCount(),
        scraperRunStatusLog.errorCount()
    );
  }

  private ScraperRunErrorLogEntity mapToEntity(ScraperRunErrorLog scraperRunError) {
    return new ScraperRunErrorLogEntity(
        scraperRunError.configurationName(),
        scraperRunError.time(),
        scraperRunError.errorCode(),
        scraperRunError.description()
    );
  }

  private Pageable extractPageSettings(StatusLogSearchQuery statusLogSearchQuery, Sort sort) {
    return statusLogSearchQuery.hasPageSetting()
        ? PageRequest.of(statusLogSearchQuery.page(), statusLogSearchQuery.pageSize(), sort)
        : null;
  }

  private Pageable extractPageSettings(ErrorLogSearchQuery errorLogSearchQuery, Sort sort) {
    return null;
  }

  private static class FindAllStatusLogSpecification implements Specification<ScraperRunStatusLogEntity> {

    private final StatusLogSearchQuery command;

    public FindAllStatusLogSpecification(StatusLogSearchQuery statusLogSearchQuery) {

      this.command = statusLogSearchQuery;
    }


    @Override
    public Predicate toPredicate(Root<ScraperRunStatusLogEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
      List<Predicate> outputPredicates = new ArrayList<>();

      if (command.hasConfigurationNames()) {
        outputPredicates.add(criteriaBuilder.in(root.get("configurationName")).value(command.configurationNames()));
      }

      if (Objects.nonNull(command.startTimeFrom())) {
        outputPredicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("startTime"), command.startTimeFrom()));
      }

      if (Objects.nonNull(command.startTimeTo())) {
        outputPredicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("startTime"), command.startTimeTo()));
      }

      if (Objects.nonNull(command.finishTimeFrom())) {
        outputPredicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("finishTime"), command.finishTimeFrom()));
      }

      if (Objects.nonNull(command.finishTimeTo())) {
        outputPredicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("finishTime"), command.finishTimeTo()));
      }

      if (Objects.nonNull(command.hasScannedEvent())) {
        final Predicate predicate = Boolean.TRUE.equals(command.hasScannedEvent())
            ? criteriaBuilder.greaterThan(root.get("scannedEventCount"), 0)
            : criteriaBuilder.equal(root.get("scannedEventCount"), 0);

        outputPredicates.add(predicate);
      } else if (Objects.nonNull(command.scannedEventCount())) {
        outputPredicates.add(criteriaBuilder.equal(root.get("scannedEventCount"), command.scannedEventCount()));
      }

      if (Objects.nonNull(command.hasErrors())) {
        final Predicate predicate = Boolean.TRUE.equals(command.hasErrors())
            ? criteriaBuilder.greaterThan(root.get("errorCount"), 0)
            : criteriaBuilder.equal(root.get("errorCount"), 0);

        outputPredicates.add(predicate);
      } else if (Objects.nonNull(command.errorCount())) {
        outputPredicates.add(criteriaBuilder.equal(root.get("errorCount"), command.scannedEventCount()));
      }

      return outputPredicates.stream()
          .reduce(criteriaBuilder::and)
          .orElse(null);
    }
  }

  private static class FindAllErrorLogSpecification implements Specification<ScraperRunErrorLogEntity> {
    private final ErrorLogSearchQuery errorLogSearchQuery;

    public FindAllErrorLogSpecification(ErrorLogSearchQuery errorLogSearchQuery) {
      this.errorLogSearchQuery = errorLogSearchQuery;
    }

    @Override
    public Predicate toPredicate(Root<ScraperRunErrorLogEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
      return null;
    }
  }
}

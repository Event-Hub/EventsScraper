package hub.event.scrapers.core;

import hub.event.scrapers.core.runlog.ErrorLogSearchQuery;
import hub.event.scrapers.core.runlog.ScraperRunErrorLog;
import hub.event.scrapers.core.runlog.ScraperRunStatusLog;
import hub.event.scrapers.core.runlog.StatusLogSearchQuery;
import org.springframework.beans.factory.annotation.Autowired;
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
class ScraperLogRepository {

  private final JpaScraperRunLogRepository jpaScraperRunLogRepository;
  private final JpaScraperRunErrorRepository jpaScraperRunErrorRepository;

  private final ScraperIdNameCache scraperIdNameCache;

  @Autowired
  public ScraperLogRepository(JpaScraperRunLogRepository jpaScraperRunLogRepository, JpaScraperRunErrorRepository jpaScraperRunErrorRepository, ScraperIdNameCache scraperIdNameCache) {
    this.jpaScraperRunLogRepository = jpaScraperRunLogRepository;
    this.jpaScraperRunErrorRepository = jpaScraperRunErrorRepository;
    this.scraperIdNameCache = scraperIdNameCache;
  }

  void save(ScraperRunStatusLog scraperRunStatusLog) {
    EntityScraperRunStatusLog entityScraperRunStatusLog = mapToEntity(scraperRunStatusLog);
    jpaScraperRunLogRepository.save(entityScraperRunStatusLog);
  }

  void save(ScraperRunErrorLog scraperRunError) {
    EntityScraperRunErrorLog entityScraperRunErrorLog = mapToEntity(scraperRunError);
    jpaScraperRunErrorRepository.save(entityScraperRunErrorLog);
  }

  List<ScraperRunErrorLog> findAllErrorLog(ErrorLogSearchQuery errorLogSearchQuery) {
    final Sort sort = Sort.by(Sort.Direction.ASC, "scraperId");
    final Specification<EntityScraperRunErrorLog> findAllSpecification = new FindAllErrorLogSpecification(errorLogSearchQuery, scraperIdNameCache);
    final Pageable pageable = extractPageSettings(errorLogSearchQuery, sort);

    return findAllErrorLog(findAllSpecification, pageable, sort)
        .stream()
        .map(this::mapToLog)
        .toList();

  }

  List<ScraperRunStatusLog> findAllStatusLog(StatusLogSearchQuery statusLogSearchQuery) {
    final Sort sort = Sort.by(Sort.Direction.ASC, "scraperId");
    final Specification<EntityScraperRunStatusLog> findAllSpecification = new FindAllStatusLogSpecification(statusLogSearchQuery, scraperIdNameCache);
    final Pageable pageable = extractPageSettings(statusLogSearchQuery, sort);

    return findAllStatusLog(findAllSpecification, pageable, sort)
        .stream()
        .map(this::mapToLog)
        .toList();
  }

  private List<EntityScraperRunStatusLog> findAllStatusLog(Specification<EntityScraperRunStatusLog> findAllSpecification, Pageable pageable, Sort sort) {
    if (Objects.isNull(pageable)) {
      return jpaScraperRunLogRepository.findAll(findAllSpecification, sort);
    }
    return jpaScraperRunLogRepository.findAll(findAllSpecification, pageable).toList();
  }

  private List<EntityScraperRunErrorLog> findAllErrorLog(Specification<EntityScraperRunErrorLog> findAllSpecification, Pageable pageable, Sort sort) {
    if (Objects.isNull(pageable)) {
      return jpaScraperRunErrorRepository.findAll(findAllSpecification, sort);
    }
    return jpaScraperRunErrorRepository.findAll(findAllSpecification, pageable).toList();
  }

  private ScraperRunStatusLog mapToLog(EntityScraperRunStatusLog entityScraperRunStatusLog) {
    final String scraperName = scraperIdNameCache.getScraperNameById(entityScraperRunStatusLog.getScraperId());

    return new ScraperRunStatusLog(
        scraperName,
        entityScraperRunStatusLog.getStartTime(),
        entityScraperRunStatusLog.getFinishTime(),
        entityScraperRunStatusLog.getScannedEventCount(),
        entityScraperRunStatusLog.getErrorCount()
    );
  }

  private ScraperRunErrorLog mapToLog(EntityScraperRunErrorLog entityScraperRunErrorLog) {
    final String scraperName = scraperIdNameCache.getScraperNameById(entityScraperRunErrorLog.getScraperId());

    return new ScraperRunErrorLog(scraperName,
        entityScraperRunErrorLog.getTime(),
        entityScraperRunErrorLog.getErrorCode(),
        entityScraperRunErrorLog.getDescription());
  }

  private EntityScraperRunStatusLog mapToEntity(ScraperRunStatusLog scraperRunStatusLog) {
    final Integer scraperId = scraperIdNameCache.getIdByScraperName(scraperRunStatusLog.configurationName());

    return new EntityScraperRunStatusLog(
        scraperId,
        scraperRunStatusLog.startTime(),
        scraperRunStatusLog.finishTime(),
        scraperRunStatusLog.scannedEventCount(),
        scraperRunStatusLog.errorCount()
    );
  }

  private EntityScraperRunErrorLog mapToEntity(ScraperRunErrorLog scraperRunError) {
    final Integer scraperId = scraperIdNameCache.getIdByScraperName(scraperRunError.configurationName());

    return new EntityScraperRunErrorLog(
        scraperId,
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
    return errorLogSearchQuery.hasPageSetting()
        ? PageRequest.of(errorLogSearchQuery.page(), errorLogSearchQuery.pageSize(), sort)
        : null;
  }

  private static class FindAllStatusLogSpecification implements Specification<EntityScraperRunStatusLog> {

    private final transient StatusLogSearchQuery searchQuery;
    private final transient ScraperIdNameCache scraperIdNameCache;

    public FindAllStatusLogSpecification(StatusLogSearchQuery statusLogSearchQuery, ScraperIdNameCache scraperIdNameCache) {

      this.searchQuery = statusLogSearchQuery;
      this.scraperIdNameCache = scraperIdNameCache;
    }


    @Override
    public Predicate toPredicate(Root<EntityScraperRunStatusLog> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
      List<Predicate> outputPredicates = new ArrayList<>();

      if (searchQuery.hasConfigurationNames()) {
        final List<Integer> idsLists = searchQuery.configurationNames()
            .stream()
            .map(scraperIdNameCache::getIdByScraperName)
            .toList();
        outputPredicates.add(criteriaBuilder.in(root.get("scraperId")).value(idsLists));
      }

      if (Objects.nonNull(searchQuery.startTimeFrom())) {
        outputPredicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("startTime"), searchQuery.startTimeFrom()));
      }

      if (Objects.nonNull(searchQuery.startTimeTo())) {
        outputPredicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("startTime"), searchQuery.startTimeTo()));
      }

      if (Objects.nonNull(searchQuery.finishTimeFrom())) {
        outputPredicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("finishTime"), searchQuery.finishTimeFrom()));
      }

      if (Objects.nonNull(searchQuery.finishTimeTo())) {
        outputPredicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("finishTime"), searchQuery.finishTimeTo()));
      }

      if (Objects.nonNull(searchQuery.hasScannedEvent())) {
        final Predicate predicate = Boolean.TRUE.equals(searchQuery.hasScannedEvent())
            ? criteriaBuilder.greaterThan(root.get("scannedEventCount"), 0)
            : criteriaBuilder.or(criteriaBuilder.equal(root.get("scannedEventCount"), 0), criteriaBuilder.isNull(root.get("scannedEventCount")));

        outputPredicates.add(predicate);
      } else if (Objects.nonNull(searchQuery.scannedEventGreaterThanOrEqualTo())) {
        outputPredicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("scannedEventCount"), searchQuery.scannedEventGreaterThanOrEqualTo()));
      }

      if (Objects.nonNull(searchQuery.hasErrors())) {
        final Predicate predicate = Boolean.TRUE.equals(searchQuery.hasErrors())
            ? criteriaBuilder.greaterThan(root.get("errorCount"), 0)
            : criteriaBuilder.or(criteriaBuilder.equal(root.get("errorCount"), 0), criteriaBuilder.isNull(root.get("errorCount")));

        outputPredicates.add(predicate);
      } else if (Objects.nonNull(searchQuery.errorCountGreaterThanOrEqualTo())) {
        outputPredicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("errorCount"), searchQuery.errorCountGreaterThanOrEqualTo()));
      }

      return outputPredicates.stream()
          .reduce(criteriaBuilder::and)
          .orElse(null);
    }
  }

  private static class FindAllErrorLogSpecification implements Specification<EntityScraperRunErrorLog> {
    private final transient ErrorLogSearchQuery searchQuery;
    private final transient ScraperIdNameCache scraperIdNameCache;

    public FindAllErrorLogSpecification(ErrorLogSearchQuery errorLogSearchQuery, ScraperIdNameCache scraperIdNameCache) {
      this.searchQuery = errorLogSearchQuery;
      this.scraperIdNameCache = scraperIdNameCache;
    }

    @Override
    public Predicate toPredicate(Root<EntityScraperRunErrorLog> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
      List<Predicate> outputPredicates = new ArrayList<>();

      if (searchQuery.hasConfigurationNames()) {
        final List<Integer> idsLists = searchQuery.configurationNames()
            .stream()
            .map(scraperIdNameCache::getIdByScraperName)
            .toList();
        outputPredicates.add(criteriaBuilder.in(root.get("scraperId")).value(idsLists));
      }

      if (Objects.nonNull(searchQuery.fromDate())) {
        outputPredicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("time"), searchQuery.fromDate()));
      }

      if (Objects.nonNull(searchQuery.toDate())) {
        outputPredicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("time"), searchQuery.toDate()));
      }

      if (Objects.nonNull(searchQuery.description())) {
        outputPredicates.add(criteriaBuilder.equal(root.get("description"), searchQuery.description()));
      }

      if (searchQuery.hasErrorCodes()) {
        outputPredicates.add(criteriaBuilder.in(root.get("errorCode")).value(searchQuery.errorCodes()));
      }

      return outputPredicates.stream()
          .reduce(criteriaBuilder::and)
          .orElse(null);
    }
  }
}

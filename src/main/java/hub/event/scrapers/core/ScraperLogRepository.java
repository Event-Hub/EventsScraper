package hub.event.scrapers.core;

import hub.event.scrapers.core.runlog.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.util.Optional;

@Repository
public class ScraperLogRepository {
  private final Logger logger = LoggerFactory.getLogger(ScraperLogRepository.class);
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
    logger.debug("Mapped EntityScraperRunStatusLog :{}", entityScraperRunStatusLog);
    jpaScraperRunLogRepository.save(entityScraperRunStatusLog);
  }

  void save(ScraperRunErrorLog scraperRunError) {
    EntityScraperRunErrorLog entityScraperRunErrorLog = mapToEntity(scraperRunError);
    logger.debug("Mapped EntityScraperRunErrorLog :{}", entityScraperRunErrorLog);
    jpaScraperRunErrorRepository.save(entityScraperRunErrorLog);
  }

  @Deprecated
  //TODO move to runlog subpackage
  public List<ScraperRunErrorLog> findAllErrorLog(ErrorLogSearchQuery errorLogSearchQuery) {
    final Sort sort = Sort.by(Sort.Direction.ASC, ScraperRunErrorLogProperties.SCRAPER_ID);
    final Specification<EntityScraperRunErrorLog> findAllSpecification = new FindAllErrorLogSpecification(errorLogSearchQuery, scraperIdNameCache);
    final Pageable pageable = extractPageSettings(errorLogSearchQuery, sort);

    return findAllErrorLog(findAllSpecification, pageable, sort)
        .stream()
        .map(this::mapToLog)
        .toList();

  }

  @Deprecated
  //TODO move to runlog subpackage
  public List<ScraperRunStatusLog> findAllStatusLog(StatusLogSearchQuery statusLogSearchQuery) {
    final Sort sort = Sort.by(Sort.Direction.ASC, ScraperRunErrorLogProperties.SCRAPER_ID);
    final Specification<EntityScraperRunStatusLog> findAllSpecification = new FindAllStatusLogSpecification(statusLogSearchQuery, scraperIdNameCache);
    final Pageable pageable = extractPageSettings(statusLogSearchQuery, sort);

    return findAllStatusLog(findAllSpecification, pageable, sort)
        .stream()
        .map(this::mapToLog)
        .toList();
  }

  @Deprecated
  //TODO move to runlog subpackage
  private List<EntityScraperRunStatusLog> findAllStatusLog(Specification<EntityScraperRunStatusLog> findAllSpecification, Pageable pageable, Sort sort) {
    if (Objects.isNull(pageable)) {
      return jpaScraperRunLogRepository.findAll(findAllSpecification, sort);
    }
    return jpaScraperRunLogRepository.findAll(findAllSpecification, pageable).toList();
  }

  @Deprecated
  //TODO move to runlog subpackage
  private List<EntityScraperRunErrorLog> findAllErrorLog(Specification<EntityScraperRunErrorLog> findAllSpecification, Pageable pageable, Sort sort) {
    if (Objects.isNull(pageable)) {
      return jpaScraperRunErrorRepository.findAll(findAllSpecification, sort);
    }
    return jpaScraperRunErrorRepository.findAll(findAllSpecification, pageable).toList();
  }

  @Deprecated
  //TODO move to runlog subpackage
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

  @Deprecated
  //TODO move to runlog subpackage
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

    private final transient Logger logger = LoggerFactory.getLogger(FindAllStatusLogSpecification.class);
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
        outputPredicates.add(criteriaBuilder.in(root.get(ScraperRunStatusLogProperties.SCRAPER_ID)).value(idsLists));
      }

      if (Objects.nonNull(searchQuery.startTimeFrom())) {
        outputPredicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get(ScraperRunStatusLogProperties.START_TIME), searchQuery.startTimeFrom()));
      }

      if (Objects.nonNull(searchQuery.startTimeTo())) {
        outputPredicates.add(criteriaBuilder.lessThanOrEqualTo(root.get(ScraperRunStatusLogProperties.START_TIME), searchQuery.startTimeTo()));
      }

      if (Objects.nonNull(searchQuery.finishTimeFrom())) {
        outputPredicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get(ScraperRunStatusLogProperties.FINISH_TIME), searchQuery.finishTimeFrom()));
      }

      if (Objects.nonNull(searchQuery.finishTimeTo())) {
        outputPredicates.add(criteriaBuilder.lessThanOrEqualTo(root.get(ScraperRunStatusLogProperties.FINISH_TIME), searchQuery.finishTimeTo()));
      }

      if (Objects.nonNull(searchQuery.hasScannedEvent())) {
        final Predicate predicate = Boolean.TRUE.equals(searchQuery.hasScannedEvent())
            ? criteriaBuilder.greaterThan(root.get(ScraperRunStatusLogProperties.SCANNED_EVENT_COUNT), 0)
            : criteriaBuilder.or(criteriaBuilder.equal(root.get(ScraperRunStatusLogProperties.SCANNED_EVENT_COUNT), 0), criteriaBuilder.isNull(root.get(ScraperRunStatusLogProperties.SCANNED_EVENT_COUNT)));

        outputPredicates.add(predicate);
      } else if (Objects.nonNull(searchQuery.scannedEventGreaterThanOrEqualTo())) {
        outputPredicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get(ScraperRunStatusLogProperties.SCANNED_EVENT_COUNT), searchQuery.scannedEventGreaterThanOrEqualTo()));
      }

      if (Objects.nonNull(searchQuery.hasErrors())) {
        final Predicate predicate = Boolean.TRUE.equals(searchQuery.hasErrors())
            ? criteriaBuilder.greaterThan(root.get(ScraperRunStatusLogProperties.ERROR_COUNT), 0)
            : criteriaBuilder.or(criteriaBuilder.equal(root.get(ScraperRunStatusLogProperties.ERROR_COUNT), 0), criteriaBuilder.isNull(root.get(ScraperRunStatusLogProperties.ERROR_COUNT)));

        outputPredicates.add(predicate);
      } else if (Objects.nonNull(searchQuery.errorCountGreaterThanOrEqualTo())) {
        outputPredicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get(ScraperRunStatusLogProperties.ERROR_COUNT), searchQuery.errorCountGreaterThanOrEqualTo()));
      }

      final Predicate outputPredicate = outputPredicates.stream()
          .reduce(criteriaBuilder::and)
          .orElse(null);

      Optional.ofNullable(outputPredicate)
          .ifPresent(predicate -> logger.debug("FindAllStatusLogSpecification output predicate: {}", predicate));

      return outputPredicate;
    }

  }

  private static class FindAllErrorLogSpecification implements Specification<EntityScraperRunErrorLog> {

    private final transient Logger logger = LoggerFactory.getLogger(FindAllErrorLogSpecification.class);
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
        outputPredicates.add(criteriaBuilder.in(root.get(ScraperRunErrorLogProperties.SCRAPER_ID)).value(idsLists));
      }

      if (Objects.nonNull(searchQuery.fromDate())) {
        outputPredicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get(ScraperRunErrorLogProperties.TIME), searchQuery.fromDate()));
      }

      if (Objects.nonNull(searchQuery.toDate())) {
        outputPredicates.add(criteriaBuilder.lessThanOrEqualTo(root.get(ScraperRunErrorLogProperties.TIME), searchQuery.toDate()));
      }

      if (Objects.nonNull(searchQuery.description())) {
        outputPredicates.add(criteriaBuilder.equal(root.get(ScraperRunErrorLogProperties.DESCRIPTION), searchQuery.description()));
      }

      if (searchQuery.hasErrorCodes()) {
        outputPredicates.add(criteriaBuilder.in(root.get(ScraperRunErrorLogProperties.ERROR_CODE)).value(searchQuery.errorCodes()));
      }

      final Predicate outputPredicate = outputPredicates.stream()
          .reduce(criteriaBuilder::and)
          .orElse(null);

      Optional.ofNullable(outputPredicate)
          .ifPresent(predicate -> logger.debug("FindAllErrorLogSpecification output predicate: {}", predicate));

      return outputPredicate;
    }
  }
}

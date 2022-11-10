package hub.event.scrapers.core.runlog;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

class EntityFindAllStatusLogSpecification implements Specification<EntityScraperRunStatusLog> {

  private final transient Logger logger = LoggerFactory.getLogger(EntityFindAllStatusLogSpecification.class);
  private final transient StatusLogSearchQuery searchQuery;


  EntityFindAllStatusLogSpecification(StatusLogSearchQuery statusLogSearchQuery) {

    this.searchQuery = statusLogSearchQuery;
  }

  @Override
  public Predicate toPredicate(Root<EntityScraperRunStatusLog> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
    final var scraperConfig = root.join("scraperConfig");

    List<Predicate> outputPredicates = new ArrayList<>();

    if (searchQuery.hasConfigurationNames()) {
      outputPredicates.add(criteriaBuilder.in(scraperConfig.get(EntityScraperRunStatusLogProperties.SCRAPER_CONFIGURATION_NAME)).value(searchQuery.configurationNames()));
    }

    if (Objects.nonNull(searchQuery.startTimeFrom())) {
      outputPredicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get(EntityScraperRunStatusLogProperties.START_TIME), searchQuery.startTimeFrom()));
    }

    if (Objects.nonNull(searchQuery.startTimeTo())) {
      outputPredicates.add(criteriaBuilder.lessThanOrEqualTo(root.get(EntityScraperRunStatusLogProperties.START_TIME), searchQuery.startTimeTo()));
    }

    if (Objects.nonNull(searchQuery.finishTimeFrom())) {
      outputPredicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get(EntityScraperRunStatusLogProperties.FINISH_TIME), searchQuery.finishTimeFrom()));
    }

    if (Objects.nonNull(searchQuery.finishTimeTo())) {
      outputPredicates.add(criteriaBuilder.lessThanOrEqualTo(root.get(EntityScraperRunStatusLogProperties.FINISH_TIME), searchQuery.finishTimeTo()));
    }

    if (Objects.nonNull(searchQuery.hasScannedEvent())) {
      final Predicate predicate = Boolean.TRUE.equals(searchQuery.hasScannedEvent())
          ? criteriaBuilder.greaterThan(root.get(EntityScraperRunStatusLogProperties.SCANNED_EVENT_COUNT), 0)
          : criteriaBuilder.or(criteriaBuilder.equal(root.get(EntityScraperRunStatusLogProperties.SCANNED_EVENT_COUNT), 0), criteriaBuilder.isNull(root.get(EntityScraperRunStatusLogProperties.SCANNED_EVENT_COUNT)));

      outputPredicates.add(predicate);
    } else if (Objects.nonNull(searchQuery.scannedEventGreaterThanOrEqualTo())) {
      outputPredicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get(EntityScraperRunStatusLogProperties.SCANNED_EVENT_COUNT), searchQuery.scannedEventGreaterThanOrEqualTo()));
    }

    if (Objects.nonNull(searchQuery.hasErrors())) {
      final Predicate predicate = Boolean.TRUE.equals(searchQuery.hasErrors())
          ? criteriaBuilder.greaterThan(root.get(EntityScraperRunStatusLogProperties.ERROR_COUNT), 0)
          : criteriaBuilder.or(criteriaBuilder.equal(root.get(EntityScraperRunStatusLogProperties.ERROR_COUNT), 0), criteriaBuilder.isNull(root.get(EntityScraperRunStatusLogProperties.ERROR_COUNT)));

      outputPredicates.add(predicate);
    } else if (Objects.nonNull(searchQuery.errorCountGreaterThanOrEqualTo())) {
      outputPredicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get(EntityScraperRunStatusLogProperties.ERROR_COUNT), searchQuery.errorCountGreaterThanOrEqualTo()));
    }

    final Predicate outputPredicate = outputPredicates.stream()
        .reduce(criteriaBuilder::and)
        .orElse(null);

    Optional.ofNullable(outputPredicate)
        .ifPresent(predicate -> logger.debug("FindAllStatusLogSpecification output predicate: {}", predicate));

    return outputPredicate;
  }

}

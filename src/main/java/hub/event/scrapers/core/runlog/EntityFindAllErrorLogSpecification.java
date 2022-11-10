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

class EntityFindAllErrorLogSpecification implements Specification<EntityScraperRunErrorLog> {

  private final transient Logger logger = LoggerFactory.getLogger(EntityFindAllErrorLogSpecification.class);
  private final transient ErrorLogSearchQuery searchQuery;

  EntityFindAllErrorLogSpecification(ErrorLogSearchQuery errorLogSearchQuery) {
    this.searchQuery = errorLogSearchQuery;
  }

  @Override
  public Predicate toPredicate(Root<EntityScraperRunErrorLog> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
    final var scraperConfig = root.join("scraperConfig");

    List<Predicate> outputPredicates = new ArrayList<>();

    if (searchQuery.hasConfigurationNames()) {
      outputPredicates.add(criteriaBuilder.in(scraperConfig.get(EntityScraperRunErrorLogProperties.SCRAPER_CONFIGURATION_NAME)).value(searchQuery.configurationNames()));
    }

    if (Objects.nonNull(searchQuery.fromDate())) {
      outputPredicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get(EntityScraperRunErrorLogProperties.TIME), searchQuery.fromDate()));
    }

    if (Objects.nonNull(searchQuery.toDate())) {
      outputPredicates.add(criteriaBuilder.lessThanOrEqualTo(root.get(EntityScraperRunErrorLogProperties.TIME), searchQuery.toDate()));
    }

    if (Objects.nonNull(searchQuery.description())) {
      outputPredicates.add(criteriaBuilder.equal(root.get(EntityScraperRunErrorLogProperties.DESCRIPTION), searchQuery.description()));
    }

    if (searchQuery.hasErrorCodes()) {
      outputPredicates.add(criteriaBuilder.in(root.get(EntityScraperRunErrorLogProperties.ERROR_CODE)).value(searchQuery.errorCodes()));
    }

    final Predicate outputPredicate = outputPredicates.stream()
        .reduce(criteriaBuilder::and)
        .orElse(null);

    Optional.ofNullable(outputPredicate)
        .ifPresent(predicate -> logger.debug("FindAllErrorLogSpecification output predicate: {}", predicate));

    return outputPredicate;
  }
}

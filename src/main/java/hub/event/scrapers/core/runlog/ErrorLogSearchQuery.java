package hub.event.scrapers.core.runlog;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

public class ErrorLogSearchQuery {
  private final List<String> configurationNames;
  private final Instant fromDate;
  private final Instant toDate;
  private final List<String> errorCodes;
  private final String description;

  private final Integer pageSize;
  private final Integer page;

  private ErrorLogSearchQuery(List<String> configurationNames, Instant fromDate, Instant toDate, List<String> errorCodes, String description, Integer page, Integer pageSize) {
    this.configurationNames = configurationNames;
    this.fromDate = fromDate;
    this.toDate = toDate;
    this.errorCodes = errorCodes;
    this.description = description;
    this.pageSize = pageSize;
    this.page = page;
  }


  public List<String> configurationNames() {
    return configurationNames;
  }

  public Instant fromDate() {
    return fromDate;
  }

  public Instant toDate() {
    return toDate;
  }

  public List<String> errorCodes() {
    return errorCodes;
  }

  public String description() {
    return description;
  }

  public Integer pageSize() {
    return pageSize;
  }

  public Integer page() {
    return page;
  }

  public boolean hasConfigurationNames() {
    return Objects.nonNull(configurationNames) && !configurationNames.isEmpty();
  }

  public boolean hasErrorCodes() {
    return Objects.nonNull(errorCodes) && !errorCodes.isEmpty();
  }

  public boolean hasPageSetting() {
    return Objects.nonNull(page) && Objects.nonNull(pageSize);
  }

  public static ErrorLogSearchQueryBuilder builder() {
    return new ErrorLogSearchQueryBuilder();
  }

  public static class ErrorLogSearchQueryBuilder {
    private List<String> configurationNames;
    private Instant fromDate;
    private Instant toDate;
    private List<String> errorCodes;
    private String description;

    private Integer pageSize;
    private Integer page;

    private ErrorLogSearchQueryBuilder() {
    }

    public ErrorLogSearchQueryBuilder description(String description) {
      this.description = description;
      return this;
    }

    public ErrorLogSearchQueryBuilder errorCodes(List<String> errorCodes) {
      this.errorCodes = errorCodes;
      return this;
    }

    public ErrorLogSearchQueryBuilder fromDate(Instant fromDate) {
      this.fromDate = fromDate;
      return this;
    }

    public ErrorLogSearchQueryBuilder toDate(Instant toDate) {
      this.toDate = toDate;
      return this;
    }

    public ErrorLogSearchQueryBuilder configurationNames(List<String> configurationNames) {
      this.configurationNames = configurationNames;
      return this;
    }

    public ErrorLogSearchQueryBuilder page(int page, int pageSize) {
      this.page = page;
      this.pageSize = pageSize;
      return this;
    }

    public ErrorLogSearchQuery build() {
      return new ErrorLogSearchQuery(configurationNames, fromDate, toDate, errorCodes, description, page, pageSize);
    }
  }
}

package hub.event.scrapers.core.runlog;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

public class StatusLogSearchQuery {
  private final List<String> configurationNames;
  private final Instant startTimeTo;
  private final Instant startTimeFrom;
  private final Instant finishTimeTo;
  private final Instant finishTimeFrom;
  private final Integer scannedEventCount;
  private final Integer errorCount;
  private final Boolean hasErrors;
  private final Boolean hasScannedEvent;
  private final Integer pageSize;
  private final Integer page;

  private StatusLogSearchQuery(List<String> configurationNames, Instant startTimeTo, Instant startTimeFrom, Instant finishTimeTo, Instant finishTime, Integer scannedEventCount, Integer errorCount, Boolean hasErrors, Boolean hasScannedEvent, Integer page, Integer pageSize) {
    this.configurationNames = configurationNames;
    this.startTimeTo = startTimeTo;
    this.startTimeFrom = startTimeFrom;
    this.finishTimeTo = finishTimeTo;
    this.finishTimeFrom = finishTime;
    this.scannedEventCount = scannedEventCount;
    this.errorCount = errorCount;
    this.hasErrors = hasErrors;
    this.hasScannedEvent = hasScannedEvent;
    this.pageSize = pageSize;
    this.page = page;
  }

  public StatusLogSearchCommandBuilder builder() {
    return new StatusLogSearchCommandBuilder();
  }

  public List<String> configurationNames() {
    return configurationNames;
  }

  public Instant startTimeFrom() {
    return startTimeFrom;
  }

  public Instant finishTimeFrom() {
    return finishTimeFrom;
  }

  public Instant startTimeTo() {
    return startTimeTo;
  }

  public Instant finishTimeTo() {
    return finishTimeTo;
  }

  public Integer scannedEventCount() {
    return scannedEventCount;
  }

  public Integer errorCount() {
    return errorCount;
  }

  public Boolean hasErrors() {
    return hasErrors;
  }

  public Boolean hasScannedEvent() {
    return hasScannedEvent;
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

  public boolean hasPageSetting() {
    return Objects.nonNull(page) && Objects.nonNull(pageSize);
  }

  static class StatusLogSearchCommandBuilder {
    private List<String> configurationNames;
    private Instant startTimeTo;
    private Instant startTimeFrom;
    private Instant finishTimeTo;
    private Instant finishTimeFrom;
    private Integer scannedEventCount;
    private Integer errorCount;
    private Boolean hasErrors;
    private Boolean hasScannedEvent;
    private Integer pageSize;
    private Integer page;

    StatusLogSearchCommandBuilder() {
    }

    public StatusLogSearchCommandBuilder configurationNames(List<String> configurationNames) {
      this.configurationNames = configurationNames;
      return this;
    }

    public StatusLogSearchCommandBuilder startTimeTo(Instant startTimeTo) {
      this.startTimeTo = startTimeTo;
      return this;
    }

    public StatusLogSearchCommandBuilder startTimeFrom(Instant startTimeFrom) {
      this.startTimeFrom = startTimeFrom;
      return this;
    }

    public StatusLogSearchCommandBuilder finishTimeTo(Instant finishTimeTo) {
      this.finishTimeTo = finishTimeTo;
      return this;
    }

    public StatusLogSearchCommandBuilder finishTimeFrom(Instant finishTimeFrom) {
      this.finishTimeFrom = finishTimeFrom;
      return this;
    }

    public StatusLogSearchCommandBuilder scannedEventCount(Integer scannedEventCount) {
      this.scannedEventCount = scannedEventCount;
      return this;
    }

    public StatusLogSearchCommandBuilder errorCount(Integer errorCount) {
      this.errorCount = errorCount;
      return this;
    }

    public StatusLogSearchCommandBuilder hasErrors(Boolean hasErrors) {
      this.hasErrors = hasErrors;
      return this;
    }

    public StatusLogSearchCommandBuilder page(Integer page) {
      this.page = page;
      return this;
    }

    public StatusLogSearchCommandBuilder pageSize(Integer pageSize) {
      this.pageSize = pageSize;
      return this;
    }

    public StatusLogSearchQuery build() {
      return new StatusLogSearchQuery(configurationNames, startTimeTo, startTimeFrom, finishTimeTo, finishTimeFrom, scannedEventCount, errorCount, hasErrors, hasScannedEvent, page, pageSize);
    }
  }

}

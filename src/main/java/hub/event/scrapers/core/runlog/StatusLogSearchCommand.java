package hub.event.scrapers.core.runlog;

import java.time.LocalDateTime;
import java.util.List;

public class StatusLogSearchCommand {
  private final List<String> configurationNames;
  private final LocalDateTime startTime;
  private final LocalDateTime finishTime;
  private final Integer scannedEventCount;
  private final Integer errorCount;
  private final Boolean hasErrors;
  private final Integer limit;
  private final Integer offset;

  private StatusLogSearchCommand(List<String> configurationNames, LocalDateTime startTime, LocalDateTime finishTime, Integer scannedEventCount, Integer errorCount, Boolean hasErrors, Integer offset, Integer limit) {
    this.configurationNames = configurationNames;
    this.startTime = startTime;
    this.finishTime = finishTime;
    this.scannedEventCount = scannedEventCount;
    this.errorCount = errorCount;
    this.hasErrors = hasErrors;
    this.limit = limit;
    this.offset = offset;
  }

  public StatusLogSearchCommandBuilder builder() {
    return new StatusLogSearchCommandBuilder();
  }

  List<String> configurationNames() {
    return configurationNames;
  }

  LocalDateTime startTime() {
    return startTime;
  }

  LocalDateTime finishTime() {
    return finishTime;
  }

  Integer scannedEventCount() {
    return scannedEventCount;
  }

  Integer errorCount() {
    return errorCount;
  }

  Boolean hasErrors() {
    return hasErrors;
  }

  Integer limit() {
    return limit;
  }

  Integer offset() {
    return offset;
  }

  class StatusLogSearchCommandBuilder {
    List<String> configurationNames;
    LocalDateTime startTime;
    LocalDateTime finishTime;
    Integer scannedEventCount;
    Integer errorCount;
    Boolean hasErrors;
    Integer limit;
    Integer offset;

    StatusLogSearchCommandBuilder() {
      this.limit = 10;
      this.offset = 0;
    }

    public StatusLogSearchCommandBuilder setConfigurationNames(List<String> configurationNames) {
      this.configurationNames = configurationNames;
      return this;
    }

    public StatusLogSearchCommandBuilder setStartTime(LocalDateTime startTime) {
      this.startTime = startTime;
      return this;
    }

    public StatusLogSearchCommandBuilder setFinishTime(LocalDateTime finishTime) {
      this.finishTime = finishTime;
      return this;
    }

    public StatusLogSearchCommandBuilder setScannedEventCount(Integer scannedEventCount) {
      this.scannedEventCount = scannedEventCount;
      return this;
    }

    public StatusLogSearchCommandBuilder setErrorCount(Integer errorCount) {
      this.errorCount = errorCount;
      return this;
    }

    public StatusLogSearchCommandBuilder setHasErrors(Boolean hasErrors) {
      this.hasErrors = hasErrors;
      return this;
    }

    public StatusLogSearchCommandBuilder setLimit(Integer limit) {
      this.limit = limit;
      return this;
    }

    public StatusLogSearchCommandBuilder setOffset(Integer offset) {
      this.offset = offset;
      return this;
    }

    public StatusLogSearchCommand build() {
      return new StatusLogSearchCommand(configurationNames, startTime, finishTime, scannedEventCount, errorCount, hasErrors, offset, limit);
    }
  }

}

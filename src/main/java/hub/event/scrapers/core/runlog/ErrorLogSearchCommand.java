package hub.event.scrapers.core.runlog;

import java.time.LocalDateTime;
import java.util.List;

public class ErrorLogSearchCommand {
  private final List<String> configurationNames;
  private final LocalDateTime fromDate;
  private final LocalDateTime toDate;
  private final List<String> errorCodes;
  private final String description;

  public ErrorLogSearchCommand(List<String> configurationNames, LocalDateTime fromDate, LocalDateTime toDate, List<String> errorCodes, String description) {
    this.configurationNames = configurationNames;
    this.fromDate = fromDate;
    this.toDate = toDate;
    this.errorCodes = errorCodes;
    this.description = description;
  }

  List<String> configurationNames() {
    return configurationNames;
  }

  LocalDateTime fromDate() {
    return fromDate;
  }

  LocalDateTime toDate() {
    return toDate;
  }

  List<String> errorCodes() {
    return errorCodes;
  }

  String description() {
    return description;
  }
}

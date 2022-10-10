package hub.event.scrapers.core.runlog;

import java.time.Instant;
import java.util.List;

public class ErrorLogSearchQuery {
  private final List<String> configurationNames;
  private final Instant fromDate;
  private final Instant toDate;
  private final List<String> errorCodes;
  private final String description;

  public ErrorLogSearchQuery(List<String> configurationNames, Instant fromDate, Instant toDate, List<String> errorCodes, String description) {
    this.configurationNames = configurationNames;
    this.fromDate = fromDate;
    this.toDate = toDate;
    this.errorCodes = errorCodes;
    this.description = description;
  }

  List<String> configurationNames() {
    return configurationNames;
  }

  Instant fromDate() {
    return fromDate;
  }

  Instant toDate() {
    return toDate;
  }

  List<String> errorCodes() {
    return errorCodes;
  }

  String description() {
    return description;
  }
}

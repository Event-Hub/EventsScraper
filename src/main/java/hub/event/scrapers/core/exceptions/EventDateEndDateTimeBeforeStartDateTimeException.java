package hub.event.scrapers.core.exceptions;

import java.time.LocalDateTime;

public class EventDateEndDateTimeBeforeStartDateTimeException extends Exception {
  public EventDateEndDateTimeBeforeStartDateTimeException(LocalDateTime startDate, LocalDateTime endDate) {
    super(String.format("End date = %s is before start date = %s", endDate, startDate));
  }
}

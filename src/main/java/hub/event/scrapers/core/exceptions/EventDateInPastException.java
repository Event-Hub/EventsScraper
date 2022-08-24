package hub.event.scrapers.core.exceptions;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class EventDateInPastException extends Exception {
  public EventDateInPastException(LocalDate date) {
    super(String.format("Past event date = %s is not allowed, only time travelers can come to it ", date));
  }

  public EventDateInPastException(LocalDateTime incorrectLocalDateTime) {
    super(String.format("Past event date = %s is not allowed, only time travelers can come to it ", incorrectLocalDateTime));
  }
}

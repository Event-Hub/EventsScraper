package hub.event.events.event.vo;

import org.apache.commons.lang3.StringUtils;

// można spróbować ich użyć również w definicjach tabelek (encjach bazodanowych)
public record Description(String description) {
  public Description(String description) {
    if (StringUtils.isBlank(description)) {
      throw new RuntimeException("Description must not be empty");
    }
    this.description = description;
  }
}

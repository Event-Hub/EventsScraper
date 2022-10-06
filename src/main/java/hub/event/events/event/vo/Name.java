package hub.event.events.event.vo;

import org.apache.commons.lang3.StringUtils;

public record Name(String name) {
  public Name(String name) {
    if (StringUtils.isBlank(name)) {
      // validation exception
      throw new RuntimeException("Name must not be empty");
    } if (name.contains("<")) {
      throw new RuntimeException("XSS Protection");
    }
    this.name = name;
  }
}

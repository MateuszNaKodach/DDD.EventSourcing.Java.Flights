package pl.zycienakodach.pragmaticflights.sdk.application.eventstream;

import java.util.List;

public record EventStream(List<Object> events) {

  public int version() {
    return events().size();
  }

}

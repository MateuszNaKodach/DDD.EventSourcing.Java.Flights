package pl.zycienakodach.pragmaticflights.shared.application;

import java.util.List;

public record EventStream(List<Object> events) {

  public int version() {
    return events().size();
  }

}

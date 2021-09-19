package pl.zycienakodach.pragmaticflights.shared.application;

import pl.zycienakodach.pragmaticflights.shared.domain.DomainEvent;

import java.util.List;

public record EventStream(List<DomainEvent> events) {

  public int version() {
    return events().size();
  }

}

package pl.zycienakodach.pragmaticflights.shared.application;

import pl.zycienakodach.pragmaticflights.shared.domain.DomainEvent;

import java.util.List;

record EventStream(List<DomainEvent> events) {

  int version() {
    return events().size();
  }

}

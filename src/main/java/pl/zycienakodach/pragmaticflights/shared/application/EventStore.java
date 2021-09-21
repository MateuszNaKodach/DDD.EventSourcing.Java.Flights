package pl.zycienakodach.pragmaticflights.shared.application;

import pl.zycienakodach.pragmaticflights.shared.domain.DomainEvent;

import java.util.List;

public interface EventStore extends EventSource {

  EventStream read(EventStreamName eventStreamName);

  void write(EventStreamName eventStreamName, List<DomainEvent> events, ExpectedStreamVersion expectedStreamVersion);
}

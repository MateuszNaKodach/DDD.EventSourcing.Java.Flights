package pl.zycienakodach.pragmaticflights.shared.application.eventstore;

import pl.zycienakodach.pragmaticflights.shared.application.message.event.EventSource;
import pl.zycienakodach.pragmaticflights.shared.application.EventStream;
import pl.zycienakodach.pragmaticflights.shared.application.EventStreamName;
import pl.zycienakodach.pragmaticflights.shared.domain.DomainEvent;

import java.util.List;

public interface EventStore extends EventSource {

  EventStream read(EventStreamName eventStreamName);

  void write(EventStreamName eventStreamName, List<DomainEvent> events, ExpectedStreamVersion expectedStreamVersion);
}

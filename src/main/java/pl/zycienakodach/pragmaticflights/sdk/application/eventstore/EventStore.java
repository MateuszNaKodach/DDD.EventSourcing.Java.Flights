package pl.zycienakodach.pragmaticflights.sdk.application.eventstore;

import pl.zycienakodach.pragmaticflights.sdk.application.message.event.EventEnvelope;
import pl.zycienakodach.pragmaticflights.sdk.application.message.event.EventSource;
import pl.zycienakodach.pragmaticflights.sdk.application.EventStream;
import pl.zycienakodach.pragmaticflights.sdk.application.EventStreamName;

import java.util.List;

public interface EventStore extends EventSource {

  EventStream read(EventStreamName eventStreamName);

  void write(EventStreamName eventStreamName, List<EventEnvelope> events, ExpectedStreamVersion expectedStreamVersion);
}

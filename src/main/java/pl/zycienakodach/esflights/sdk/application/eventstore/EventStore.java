package pl.zycienakodach.esflights.sdk.application.eventstore;

import pl.zycienakodach.esflights.sdk.application.message.event.EventEnvelope;
import pl.zycienakodach.esflights.sdk.application.message.event.EventSource;
import pl.zycienakodach.esflights.sdk.application.eventstream.EventStream;
import pl.zycienakodach.esflights.sdk.application.eventstream.EventStreamName;

import java.util.List;

public interface EventStore extends EventSource {

  EventStream read(EventStreamName eventStreamName);

  void write(EventStreamName eventStreamName, List<EventEnvelope> events, ExpectedStreamVersion expectedStreamVersion);
}

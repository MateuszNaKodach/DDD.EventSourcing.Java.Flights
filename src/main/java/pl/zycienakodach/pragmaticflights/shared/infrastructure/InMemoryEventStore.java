package pl.zycienakodach.pragmaticflights.shared.infrastructure;

import pl.zycienakodach.pragmaticflights.shared.application.message.event.EventBus;
import pl.zycienakodach.pragmaticflights.shared.application.message.event.EventEnvelope;
import pl.zycienakodach.pragmaticflights.shared.application.message.event.EventHandler;
import pl.zycienakodach.pragmaticflights.shared.application.eventstore.EventStore;
import pl.zycienakodach.pragmaticflights.shared.application.EventStream;
import pl.zycienakodach.pragmaticflights.shared.application.EventStreamName;
import pl.zycienakodach.pragmaticflights.shared.application.eventstore.ExpectedStreamVersion;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Stream;

import static java.util.Collections.emptyList;

public class InMemoryEventStore implements EventStore {

  private final ConcurrentMap<EventStreamName, List<EventEnvelope>> streams = new ConcurrentHashMap<>();
  private final EventBus eventBus;

  public InMemoryEventStore(EventBus eventBus) {
    this.eventBus = eventBus;
  }

  @Override
  public <T> void subscribe(Class<T> eventType, EventHandler<T> handler) {
    eventBus.subscribe(eventType, handler);
  }

  @Override
  public EventStream read(EventStreamName eventStreamName) {
    var events = streams.getOrDefault(eventStreamName, emptyList());
    return new EventStream(events.stream().map(EventEnvelope::event).toList());
  }

  @Override
  public void write(EventStreamName eventStreamName, List<EventEnvelope> events, ExpectedStreamVersion expectedStreamVersion) {
    streams.compute(eventStreamName, (__, streamEvents) -> {
      var currentStreamVersion = Optional.ofNullable(streamEvents).map(List::size).orElse(0);
      eventStreamWriteOptimisticLocking(expectedStreamVersion, currentStreamVersion);
      return streamEvents == null ? List.copyOf(events) : Stream.concat(streamEvents.stream(), events.stream()).toList();
    });
    this.eventBus.publishAll(events);
  }

  private void eventStreamWriteOptimisticLocking(ExpectedStreamVersion expectedStreamVersion, int currentStreamVersion) {
    var isExpectedVersion = switch (expectedStreamVersion) {
      case ExpectedStreamVersion.NotExists ignored:
        yield currentStreamVersion == 0;
      case ExpectedStreamVersion.Exactly v:
        yield currentStreamVersion == v.raw();
      case ExpectedStreamVersion.Any ignored:
        yield true;
      default:
        throw new IllegalStateException("Unexpected value: " + expectedStreamVersion);
    };
    if (!isExpectedVersion) {
      throw new IllegalStateException("Expected event stream version is " + expectedStreamVersion + " but current is " + currentStreamVersion);
    }
  }

  ;
}

package pl.zycienakodach.pragmaticflights.shared.infrastructure;

import pl.zycienakodach.pragmaticflights.shared.application.EventBus;
import pl.zycienakodach.pragmaticflights.shared.application.EventSource;
import pl.zycienakodach.pragmaticflights.shared.application.EventStore;
import pl.zycienakodach.pragmaticflights.shared.application.EventStream;
import pl.zycienakodach.pragmaticflights.shared.application.EventStreamName;
import pl.zycienakodach.pragmaticflights.shared.application.ExpectedStreamVersion;
import pl.zycienakodach.pragmaticflights.shared.domain.DomainEvent;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static java.util.Collections.emptyList;

class InMemoryEventStore implements EventStore, EventSource {

  private final EventBus eventBus;
  private final ConcurrentMap<EventStreamName, List<DomainEvent>> streams = new ConcurrentHashMap<>();

  InMemoryEventStore(EventBus eventBus) {
    this.eventBus = eventBus;
  }

  @Override
  public <T> void subscribe(Class<T> eventType, Consumer<T> handler) {
    this.eventBus.subscribe(eventType, handler);
  }

  @Override
  public EventStream read(EventStreamName eventStreamName) {
    var events = streams.getOrDefault(eventStreamName, emptyList());
    return new EventStream(events);
  }

  @Override
  public void write(EventStreamName eventStreamName, List<DomainEvent> events, ExpectedStreamVersion expectedStreamVersion) {
    streams.compute(eventStreamName, (__, streamEvents) -> {
      var currentStreamVersion = new EventStream(streamEvents).version();
      eventStreamWriteOptimisticLocking(expectedStreamVersion, currentStreamVersion);
      return streamEvents == null ? List.copyOf(events) : Stream.concat(streamEvents.stream(), events.stream()).toList();
    });
    eventBus.publishAll(events);
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
    if(!isExpectedVersion){
      throw new IllegalStateException("Expected event stream version is " + expectedStreamVersion + " but current is " + currentStreamVersion);
    }
  }

  ;
}

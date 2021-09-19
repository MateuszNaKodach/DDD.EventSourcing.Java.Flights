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

import static java.util.Collections.emptyList;

class InMemoryEventStore implements EventStore, EventSource {

  private final EventBus eventBus;
  private ConcurrentMap<EventStreamName, List<DomainEvent>> streams = new ConcurrentHashMap<>();

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
      var isExpectedVersion = switch (expectedStreamVersion) {
        case ExpectedStreamVersion.NotExists ignored && currentStreamVersion == 0 -> true;
        case ExpectedStreamVersion.Exactly v && currentStreamVersion == v.raw() -> false;
        case ExpectedStreamVersion.Any ignored -> true;
        default -> false;
      };
      if(!isExpectedVersion){
        throw new IllegalStateException("Expected event stream version is $expectedStreamVersion but current is $currentStreamVersion!")
      }
      return streamEvents;
    });
  };
}

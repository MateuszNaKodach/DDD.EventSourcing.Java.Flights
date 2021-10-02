package pl.zycienakodach.pragmaticflights.sdk.infrastructure.eventstore;

import pl.zycienakodach.pragmaticflights.sdk.application.message.event.EventBus;
import pl.zycienakodach.pragmaticflights.sdk.application.message.event.EventEnvelope;
import pl.zycienakodach.pragmaticflights.sdk.application.message.event.EventHandler;
import pl.zycienakodach.pragmaticflights.sdk.application.eventstore.EventStore;
import pl.zycienakodach.pragmaticflights.sdk.application.eventstream.EventStream;
import pl.zycienakodach.pragmaticflights.sdk.application.eventstream.EventStreamName;
import pl.zycienakodach.pragmaticflights.sdk.application.eventstore.ExpectedStreamVersion;

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
    var isExpectedVersion = isExpectedVersion(expectedStreamVersion, currentStreamVersion);
    if (!isExpectedVersion) {
      throw new IllegalStateException("Expected event stream version is " + expectedStreamVersion + " but current is " + currentStreamVersion);
    }
  }

  private boolean isExpectedVersion(ExpectedStreamVersion expectedStreamVersion, int currentStreamVersion) {
    if(expectedStreamVersion instanceof ExpectedStreamVersion.NotExists){
      return currentStreamVersion == 0;
    }
    if(expectedStreamVersion instanceof ExpectedStreamVersion.Exactly){
      return currentStreamVersion == ((ExpectedStreamVersion.Exactly) expectedStreamVersion).raw();
    }
    return expectedStreamVersion instanceof ExpectedStreamVersion.Any;
  }

  ;
}

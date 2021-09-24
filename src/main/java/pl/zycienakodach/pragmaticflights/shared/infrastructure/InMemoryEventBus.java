package pl.zycienakodach.pragmaticflights.shared.infrastructure;

import pl.zycienakodach.pragmaticflights.shared.application.message.event.EventBus;
import pl.zycienakodach.pragmaticflights.shared.application.message.event.EventEnvelope;
import pl.zycienakodach.pragmaticflights.shared.application.message.event.EventHandler;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import static java.util.Collections.emptyList;

public class InMemoryEventBus implements EventBus {

  private final ConcurrentHashMap<Class<?>, List<EventHandler<?>>> handlers = new ConcurrentHashMap<>();

  @Override
  public void publish(EventEnvelope eventEnvelope) {
    Class<?> eventType = eventEnvelope.getClass();
    var eventHandlers = handlers.getOrDefault(eventType, emptyList());
    //noinspection unchecked
    eventHandlers.stream()
        .map(handler -> (EventHandler<Object>) handler)
        .forEach(handler -> handler.accept(eventEnvelope.event(), eventEnvelope.metadata()));
  }

  @Override
  public <T> void subscribe(Class<T> eventType, EventHandler<T> handler) {
    handlers.compute(
        eventType,
        (__, handlers) -> handlers == null
                ? List.of(handler)
                : Stream.concat(handlers.stream(), Stream.of(handler)).toList()
    );
  }

}

package pl.zycienakodach.pragmaticflights.shared.infrastructure;

import pl.zycienakodach.pragmaticflights.shared.application.EventBus;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static java.util.Collections.emptyList;

class InMemoryEventBus implements EventBus {

  private final ConcurrentHashMap<Class<?>, List<Consumer<?>>> handlers = new ConcurrentHashMap<>();

  @Override
  public <T> void publish(T event) {
    Class<?> eventType = event.getClass();
    var eventHandlers = handlers.getOrDefault(eventType, emptyList());
    //noinspection unchecked
    eventHandlers.stream()
        .map(handler -> (Consumer<T>) handler)
        .forEach(handler -> handler.accept(event));
  }

  @Override
  public <T> void subscribe(Class<T> eventType, Consumer<T> handler) {
    handlers.compute(
        eventType,
        (__, handlers) -> handlers == null
                ? List.of(handler)
                : Stream.concat(handlers.stream(), Stream.of(handler)).toList()
    );
  }

}

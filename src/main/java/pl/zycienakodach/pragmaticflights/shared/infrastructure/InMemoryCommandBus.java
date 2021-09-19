package pl.zycienakodach.pragmaticflights.shared.infrastructure;

import pl.zycienakodach.pragmaticflights.shared.application.CommandBus;

import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

final class InMemoryCommandBus implements CommandBus {

  private final ConcurrentHashMap<Class<?>, Consumer<?>> handlers = new ConcurrentHashMap<>();

  @Override
  public <T> void register(Class<T> commandType, Consumer<T> handler) {
    var registeredHandler = handlers.putIfAbsent(commandType, handler);
    if (registeredHandler != null) {
      throw new RuntimeException("Multiple handlers not allowed for " + commandType.getSimpleName());
    }
  }

  @Override
  public <T> void handle(T command) {
    Class<?> commandType = command.getClass();
    var handler = handlers.get(commandType);
    if(handler == null){
      throw new RuntimeException("Missing handler for " + commandType.getSimpleName());
    }
    //noinspection unchecked
    ((Consumer<T>)handler).accept(command);
  }


}

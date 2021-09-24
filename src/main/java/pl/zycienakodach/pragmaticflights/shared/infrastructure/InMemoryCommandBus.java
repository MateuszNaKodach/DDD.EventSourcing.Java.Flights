package pl.zycienakodach.pragmaticflights.shared.infrastructure;

import pl.zycienakodach.pragmaticflights.shared.application.message.command.CommandBus;
import pl.zycienakodach.pragmaticflights.shared.application.message.command.CommandHandler;
import pl.zycienakodach.pragmaticflights.shared.application.message.command.CommandMetadata;
import pl.zycienakodach.pragmaticflights.shared.application.message.command.CommandResult;

import java.util.concurrent.ConcurrentHashMap;

public final class InMemoryCommandBus implements CommandBus {

  private final ConcurrentHashMap<Class<?>, CommandHandler<?>> handlers = new ConcurrentHashMap<>();

  @Override
  public <T> void registerHandler(Class<T> commandType, CommandHandler<T> handler) {
    var registeredHandler = handlers.putIfAbsent(commandType, handler);
    if (registeredHandler != null) {
      throw new RuntimeException("Multiple handlers not allowed for " + commandType.getSimpleName());
    }
  }

  @Override
  public <T> CommandResult execute(T command, CommandMetadata metadata) {
    Class<?> commandType = command.getClass();
    var handler = handlers.get(commandType);
    if (handler == null) {
      throw new RuntimeException("Missing handler for " + commandType.getSimpleName());
    }
    //noinspection unchecked
    return ((CommandHandler<T>) handler).apply(command, metadata);
  }


}

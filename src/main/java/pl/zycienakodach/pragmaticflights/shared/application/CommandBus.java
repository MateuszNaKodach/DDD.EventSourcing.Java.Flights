package pl.zycienakodach.pragmaticflights.shared.application;

import java.util.function.Function;

public interface CommandBus {

  <T> void registerHandler(Class<T> commandType, CommandHandler<T> handler);

  <T> CommandResult execute(T command);
}

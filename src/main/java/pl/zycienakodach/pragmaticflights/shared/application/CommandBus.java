package pl.zycienakodach.pragmaticflights.shared.application;

import java.util.function.Consumer;

public interface CommandBus {

  <T> void registerHandler(Class<T> commandType, Consumer<T> handler);

  <T> void execute(T command);
}

package pl.zycienakodach.pragmaticflights.shared.application;

import java.util.function.Consumer;

public interface CommandBus {
  <T> void register(Class<T> commandType, Consumer<T> handler);

  <T> void handle(T command);
}

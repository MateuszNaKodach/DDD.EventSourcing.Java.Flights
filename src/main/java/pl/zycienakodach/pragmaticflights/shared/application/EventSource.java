package pl.zycienakodach.pragmaticflights.shared.application;

import java.util.function.Consumer;

public interface EventSource {

  <T> void subscribe(Class<T> eventType, Consumer<T> handler);

}

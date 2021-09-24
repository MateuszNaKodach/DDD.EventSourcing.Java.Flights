package pl.zycienakodach.pragmaticflights.shared.application.message.event;

import pl.zycienakodach.pragmaticflights.shared.application.message.event.EventHandler;

public interface EventSource {

  <T> void subscribe(Class<T> eventType, EventHandler<T> handler);

}

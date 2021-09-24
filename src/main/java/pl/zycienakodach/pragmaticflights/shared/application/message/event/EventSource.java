package pl.zycienakodach.pragmaticflights.shared.application.message.event;

public interface EventSource {

  <T> void subscribe(Class<T> eventType, EventHandler<T> handler);

}

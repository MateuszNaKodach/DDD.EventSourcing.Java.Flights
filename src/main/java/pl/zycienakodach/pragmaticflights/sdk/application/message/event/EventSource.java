package pl.zycienakodach.pragmaticflights.sdk.application.message.event;

public interface EventSource {

  <T> void subscribe(Class<T> eventType, EventHandler<T> handler);

}

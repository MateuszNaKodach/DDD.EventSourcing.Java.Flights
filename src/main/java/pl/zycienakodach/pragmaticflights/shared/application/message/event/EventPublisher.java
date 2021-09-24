package pl.zycienakodach.pragmaticflights.shared.application.message.event;

import java.util.List;

public interface EventPublisher {

  default <T> void publishAll(List<T> events) {
    events.forEach(this::publish);
  }

  <T> void publish(T event);

}

package pl.zycienakodach.esflights.sdk.application.message.event;

import java.util.List;

public interface EventPublisher {

  default void publish(Object event, EventMetadata metadata){
    publish(new EventEnvelope(event, metadata));
  }

  default void publishAll(List<EventEnvelope> eventEnvelopes) {
    eventEnvelopes.forEach(this::publish);
  }

  void publish(EventEnvelope eventEnvelope);

}

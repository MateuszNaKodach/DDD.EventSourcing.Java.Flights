package pl.zycienakodach.pragmaticflights.sdk.infrastructure.message.event;

import pl.zycienakodach.pragmaticflights.sdk.application.message.CausationId;
import pl.zycienakodach.pragmaticflights.sdk.application.message.event.EventBus;
import pl.zycienakodach.pragmaticflights.sdk.application.message.event.EventEnvelope;
import pl.zycienakodach.pragmaticflights.sdk.application.message.event.EventHandler;

import java.util.ArrayList;
import java.util.List;

public class RecordingEventBus implements EventBus {

  private final EventBus next;
  private final List<EventEnvelope> publishedEvents = new ArrayList<>();

  public RecordingEventBus(EventBus next) {
    this.next = next;
  }

  @Override
  public void publish(EventEnvelope eventEnvelope) {
    next.publish(eventEnvelope);
    publishedEvents.add(eventEnvelope);
  }

  @Override
  public <T> void subscribe(Class<T> eventType, EventHandler<T> handler) {
    next.subscribe(eventType, handler);
  }

  public Object lastPublishedEvent() {
    if (publishedEvents.size() == 0) {
      return null;
    }
    return publishedEvents.get(publishedEvents.size() - 1).event();
  }

  public List<Object> eventsCausedBy(CausationId causationId){
    return this.publishedEvents.stream()
        .filter(envelope -> envelope.metadata().causationId().equals(causationId))
        .map(EventEnvelope::event)
        .toList();
  }

}

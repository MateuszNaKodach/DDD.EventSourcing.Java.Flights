package pl.zycienakodach.esflights.sdk.infrastructure.message.event;

import pl.zycienakodach.esflights.sdk.application.message.event.EventBus;
import pl.zycienakodach.esflights.sdk.application.message.event.EventEnvelope;
import pl.zycienakodach.esflights.sdk.application.message.event.EventHandler;

import java.util.ArrayList;
import java.util.List;

public class RecordingEventBus implements EventBus, RecordedEvents {

  private final EventBus next;
  private final List<EventEnvelope> publishedEvents = new ArrayList<>();

  public RecordingEventBus(EventBus next) {
    this.next = next;
  }

  @Override
  public List<EventEnvelope> publishedEvents() {
    return publishedEvents;
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

}

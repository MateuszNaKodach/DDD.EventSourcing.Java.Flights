package pl.zycienakodach.pragmaticflights.sdk.infrastructure.message.event;

import pl.zycienakodach.pragmaticflights.sdk.application.message.CausationId;
import pl.zycienakodach.pragmaticflights.sdk.application.message.command.CommandId;
import pl.zycienakodach.pragmaticflights.sdk.application.message.event.EventEnvelope;
import pl.zycienakodach.pragmaticflights.sdk.application.message.event.EventMetadata;

import java.util.List;

public interface RecordedMessages {
  List<EventEnvelope> publishedEvents();

  default EventEnvelope lastPublishedEventEnvelope() {
    if (publishedEvents().size() == 0) {
      return null;
    }
    return publishedEvents().get(publishedEvents().size() - 1);
  }

  default Object lastPublishedEvent() {
    if (publishedEvents().size() == 0) {
      return null;
    }
    var envelope = publishedEvents().get(publishedEvents().size() - 1);
    return envelope.event();
  }

  default EventMetadata lastPublishedEventMetadata() {
    EventEnvelope envelope = lastPublishedEventEnvelope();
    if (envelope == null) return null;
    return envelope.metadata();
  }

  default List<Object> eventsCausedBy(CausationId causationId) {
    return publishedEvents().stream()
        .filter(envelope -> envelope.metadata().causationId().equals(causationId))
        .map(EventEnvelope::event)
        .toList();
  }

  default List<Object> eventsCausedBy(CommandId commandId) {
    return publishedEvents().stream()
        .filter(envelope -> envelope.metadata().causationId().equals(new CausationId(commandId.raw())))
        .map(EventEnvelope::event)
        .toList();
  }

  default Object lastEventCausedBy(CommandId commandId) {
    final List<Object> eventsCausedByCommand = eventsCausedBy(commandId);
    return eventsCausedByCommand.isEmpty() ? null : eventsCausedByCommand.get(eventsCausedByCommand.size() - 1);
  }
}

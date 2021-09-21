package pl.zycienakodach.pragmaticflights.shared.domain;

import java.time.Instant;

public interface DomainEvent {
  EventId eventId();

  Instant occurredAt();

  EventType type();

  EventMetadata metadata();
}


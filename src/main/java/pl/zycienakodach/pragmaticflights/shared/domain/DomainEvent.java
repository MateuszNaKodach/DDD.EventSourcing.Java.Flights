package pl.zycienakodach.pragmaticflights.shared.domain;

import java.time.Instant;

public interface DomainEvent {
  Instant occurredAt();

  EventType type();
}


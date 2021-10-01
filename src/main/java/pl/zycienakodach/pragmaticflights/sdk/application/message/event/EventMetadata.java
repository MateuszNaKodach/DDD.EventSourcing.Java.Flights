package pl.zycienakodach.pragmaticflights.sdk.application.message.event;

import pl.zycienakodach.pragmaticflights.sdk.application.tenant.TenantId;
import pl.zycienakodach.pragmaticflights.sdk.application.message.CausationId;
import pl.zycienakodach.pragmaticflights.sdk.application.message.CorrelationId;
import pl.zycienakodach.pragmaticflights.sdk.application.message.MessageMetadata;

import java.time.Instant;

public class EventMetadata extends MessageMetadata {
  //TODO: Add to map (as messageId?)
  private final EventId eventId;

  public EventMetadata(EventId eventId, Instant timestamp, TenantId tenantId, CorrelationId correlationId, CausationId causationId) {
    super(timestamp, tenantId, correlationId, causationId);
    this.eventId = eventId;
  }

  public EventMetadata(EventId eventId, Instant timestamp, TenantId tenantId, CorrelationId correlationId) {
    super(timestamp, tenantId, correlationId, new CausationId(correlationId.raw()));
    this.eventId = eventId;
  }

  public EventId eventId() {
    return eventId;
  }
}

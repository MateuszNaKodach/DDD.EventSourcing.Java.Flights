package pl.zycienakodach.pragmaticflights.sdk.application.message.event;

import pl.zycienakodach.pragmaticflights.sdk.application.message.CausationId;
import pl.zycienakodach.pragmaticflights.sdk.application.message.CorrelationId;
import pl.zycienakodach.pragmaticflights.sdk.application.message.MessageMetadata;
import pl.zycienakodach.pragmaticflights.sdk.application.tenant.TenantId;

import java.time.Instant;

public class EventMetadata extends MessageMetadata {

  private static final String EVENT_ID_METADATA_KEY = "EventId";

  public EventMetadata(EventId eventId, Instant timestamp, TenantId tenantId, CorrelationId correlationId, CausationId causationId) {
    super(timestamp, tenantId, correlationId, causationId);
    this.hashMap.put(EVENT_ID_METADATA_KEY, eventId.raw());
  }

  public EventMetadata(EventId eventId, Instant timestamp, TenantId tenantId, CorrelationId correlationId) {
    super(timestamp, tenantId, correlationId, new CausationId(correlationId.raw()));
    this.hashMap.put(EVENT_ID_METADATA_KEY, eventId.raw());
  }

  public EventId eventId() {
    return new EventId(this.hashMap.get(EVENT_ID_METADATA_KEY));
  }
}

package pl.zycienakodach.pragmaticflights.shared.application.message.event;

import pl.zycienakodach.pragmaticflights.shared.application.tenant.TenantId;
import pl.zycienakodach.pragmaticflights.shared.application.message.CausationId;
import pl.zycienakodach.pragmaticflights.shared.application.message.CorrelationId;
import pl.zycienakodach.pragmaticflights.shared.application.message.MessageMetadata;

import java.time.Instant;

public class EventMetadata extends MessageMetadata {
  private final EventId eventId;
  private final Instant timestamp;

  public EventMetadata(EventId eventId, Instant timestamp, TenantId tenantId, CorrelationId correlationId, CausationId causationId) {
    super(tenantId, correlationId, causationId);
    this.eventId = eventId;
    this.timestamp = timestamp;
  }

  public EventMetadata(EventId eventId, Instant timestamp, TenantId tenantId, CorrelationId correlationId) {
    super(tenantId, correlationId, new CausationId(correlationId.raw()));
    this.eventId = eventId;
    this.timestamp = timestamp;
  }

//  static EventMetadata fromCommand(EventId eventId, CommandMetadata commandMetadata){
//
//  }

  public EventId eventId() {
    return eventId;
  }

  public Instant timestamp(){
    return timestamp;
  }

}

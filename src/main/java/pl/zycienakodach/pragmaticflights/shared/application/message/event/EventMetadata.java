package pl.zycienakodach.pragmaticflights.shared.application.message.event;

import pl.zycienakodach.pragmaticflights.shared.application.tenant.TenantId;
import pl.zycienakodach.pragmaticflights.shared.application.message.CausationId;
import pl.zycienakodach.pragmaticflights.shared.application.message.CorrelationId;
import pl.zycienakodach.pragmaticflights.shared.application.message.MessageMetadata;

public class EventMetadata extends MessageMetadata {
  private final EventId eventId;

  public EventMetadata(EventId eventId, TenantId tenantId, CorrelationId correlationId, CausationId causationId) {
    super(tenantId, correlationId, causationId);
    this.eventId = eventId;
  }

//  static EventMetadata fromCommand(EventId eventId, CommandMetadata commandMetadata){
//
//  }

  public EventId eventId() {
    return eventId;
  }
}

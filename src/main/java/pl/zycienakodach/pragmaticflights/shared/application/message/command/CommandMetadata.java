package pl.zycienakodach.pragmaticflights.shared.application.message.command;

import pl.zycienakodach.pragmaticflights.shared.application.tenant.TenantId;
import pl.zycienakodach.pragmaticflights.shared.application.message.CausationId;
import pl.zycienakodach.pragmaticflights.shared.application.message.CorrelationId;
import pl.zycienakodach.pragmaticflights.shared.application.message.MessageMetadata;

public class CommandMetadata extends MessageMetadata {

  public CommandMetadata(TenantId tenantId, CorrelationId correlationId) {
    super(tenantId, correlationId, new CausationId(correlationId.raw()));
  }

  public CommandMetadata(TenantId tenantId, CorrelationId correlationId, CausationId causationId) {
    super(tenantId, correlationId, causationId);
  }
}

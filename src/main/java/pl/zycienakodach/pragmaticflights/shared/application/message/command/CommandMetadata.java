package pl.zycienakodach.pragmaticflights.shared.application.message.command;

import pl.zycienakodach.pragmaticflights.shared.application.tenant.TenantId;
import pl.zycienakodach.pragmaticflights.shared.application.message.CausationId;
import pl.zycienakodach.pragmaticflights.shared.application.message.CorrelationId;
import pl.zycienakodach.pragmaticflights.shared.application.message.MessageMetadata;

public class CommandMetadata extends MessageMetadata {

  private final CommandId commandId;

  public CommandMetadata(CommandId commandId, TenantId tenantId, CorrelationId correlationId) {
    super(tenantId, correlationId, new CausationId(correlationId.raw()));
    this.commandId = commandId;
  }

  public CommandMetadata(CommandId commandId, TenantId tenantId, CorrelationId correlationId, CausationId causationId) {
    super(tenantId, correlationId, causationId);
    this.commandId = commandId;
  }

  public CommandMetadata(CommandId commandId, TenantId tenantId) {
    super(tenantId, new CorrelationId(commandId.raw()), new CausationId(commandId.raw()));
    this.commandId = commandId;
  }

  public CommandId commandId() {
    return commandId;
  }
}
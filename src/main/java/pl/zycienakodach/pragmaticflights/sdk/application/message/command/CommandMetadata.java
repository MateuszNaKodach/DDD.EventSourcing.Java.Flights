package pl.zycienakodach.pragmaticflights.sdk.application.message.command;

import pl.zycienakodach.pragmaticflights.sdk.application.tenant.TenantId;
import pl.zycienakodach.pragmaticflights.sdk.application.message.CausationId;
import pl.zycienakodach.pragmaticflights.sdk.application.message.CorrelationId;
import pl.zycienakodach.pragmaticflights.sdk.application.message.MessageMetadata;

import java.time.Instant;

public class CommandMetadata extends MessageMetadata {

  private final CommandId commandId;

  public CommandMetadata(CommandId commandId, Instant timestamp, TenantId tenantId, CorrelationId correlationId) {
    super(timestamp, tenantId, correlationId, new CausationId(correlationId.raw()));
    this.commandId = commandId;
  }

  public CommandMetadata(CommandId commandId, Instant timestamp, TenantId tenantId, CorrelationId correlationId, CausationId causationId) {
    super(timestamp, tenantId, correlationId, causationId);
    this.commandId = commandId;
  }

  public CommandMetadata(CommandId commandId, Instant timestamp, TenantId tenantId) {
    super(timestamp, tenantId, new CorrelationId(commandId.raw()), new CausationId(commandId.raw()));
    this.commandId = commandId;
  }

  public CommandId commandId() {
    return commandId;
  }
}

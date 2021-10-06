package pl.zycienakodach.pragmaticflights.sdk.application.message.command;

import pl.zycienakodach.pragmaticflights.sdk.application.message.CausationId;
import pl.zycienakodach.pragmaticflights.sdk.application.message.CorrelationId;
import pl.zycienakodach.pragmaticflights.sdk.application.message.MessageMetadata;
import pl.zycienakodach.pragmaticflights.sdk.application.tenant.TenantId;

import java.time.Instant;

public class CommandMetadata extends MessageMetadata {

  private static final String COMMAND_ID_METADATA_KEY = "CommandId";

  public CommandMetadata(CommandId commandId, Instant timestamp, TenantId tenantId, CorrelationId correlationId) {
    super(timestamp, tenantId, correlationId, new CausationId(correlationId.raw()));
    this.hashMap.put(COMMAND_ID_METADATA_KEY, commandId.raw());
  }

  public CommandMetadata(CommandId commandId, Instant timestamp, TenantId tenantId, CorrelationId correlationId, CausationId causationId) {
    super(timestamp, tenantId, correlationId, causationId);
    this.hashMap.put(COMMAND_ID_METADATA_KEY, commandId.raw());
  }

  public CommandMetadata(CommandId commandId, Instant timestamp, TenantId tenantId) {
    super(timestamp, tenantId, new CorrelationId(commandId.raw()), new CausationId(commandId.raw()));
    this.hashMap.put(COMMAND_ID_METADATA_KEY, commandId.raw());
  }

  public CommandId commandId() {
    return new CommandId(this.hashMap.get(COMMAND_ID_METADATA_KEY));
  }
}

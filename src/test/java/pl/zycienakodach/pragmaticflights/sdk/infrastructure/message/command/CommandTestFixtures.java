package pl.zycienakodach.pragmaticflights.sdk.infrastructure.message.command;

import pl.zycienakodach.pragmaticflights.sdk.application.IdGenerator;
import pl.zycienakodach.pragmaticflights.sdk.application.message.command.CommandId;
import pl.zycienakodach.pragmaticflights.sdk.application.message.command.CommandMetadata;
import pl.zycienakodach.pragmaticflights.sdk.application.tenant.TenantId;

import java.util.UUID;

public class CommandTestFixtures {

  public static CommandMetadata aCommandMetadata() {
    IdGenerator generateId = () -> UUID.randomUUID().toString();
    return new CommandMetadata(
        new CommandId(generateId.get()),
        new TenantId("TestTenant")
    );
  }

  public static CommandMetadata aCommandMetadata(TenantId tenantId) {
    IdGenerator generateId = () -> UUID.randomUUID().toString();
    return new CommandMetadata(
        new CommandId(generateId.get()),
        tenantId
    );
  }

}

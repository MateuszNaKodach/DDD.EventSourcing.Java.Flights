package pl.zycienakodach.pragmaticflights.shared.infrastructure.message.command;

import pl.zycienakodach.pragmaticflights.shared.application.IdGenerator;
import pl.zycienakodach.pragmaticflights.shared.application.message.command.CommandId;
import pl.zycienakodach.pragmaticflights.shared.application.message.command.CommandMetadata;
import pl.zycienakodach.pragmaticflights.shared.application.tenant.TenantId;

import java.util.UUID;

public class CommandTestFixtures {

  public static CommandMetadata aCommandMetadata() {
    IdGenerator generateId = () -> UUID.randomUUID().toString();
    return new CommandMetadata(
        new CommandId(generateId.get()),
        new TenantId(generateId.get())
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

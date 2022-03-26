package pl.zycienakodach.esflights.sdk.infrastructure.message.command;

import pl.zycienakodach.esflights.sdk.application.message.command.CommandBus;
import pl.zycienakodach.esflights.sdk.application.message.command.CommandHandler;
import pl.zycienakodach.esflights.sdk.application.message.command.CommandMetadata;
import pl.zycienakodach.esflights.sdk.application.message.command.CommandResult;

import java.util.ArrayList;
import java.util.List;

public class RecordingCommandBus implements CommandBus, RecordedCommands {

  private final CommandBus next;
  private final List<CommandEnvelope> executedCommands = new ArrayList<>();

  public RecordingCommandBus(CommandBus next) {
    this.next = next;
  }

  @Override
  public <T> void registerHandler(Class<T> commandType, CommandHandler<T> handler) {
    next.registerHandler(commandType, handler);
  }

  @Override
  public <T> CommandResult execute(T command, CommandMetadata metadata) {
    var result = next.execute(command, metadata);
    executedCommands.add(new CommandEnvelope(command, metadata));
    return result;
  }

  @Override
  public List<CommandEnvelope> executedCommands() {
    return executedCommands;
  }
}

package pl.zycienakodach.pragmaticflights.sdk.infrastructure.message.command;

import pl.zycienakodach.pragmaticflights.sdk.application.message.CausationId;
import pl.zycienakodach.pragmaticflights.sdk.application.message.command.CommandBus;
import pl.zycienakodach.pragmaticflights.sdk.application.message.command.CommandHandler;
import pl.zycienakodach.pragmaticflights.sdk.application.message.command.CommandMetadata;
import pl.zycienakodach.pragmaticflights.sdk.application.message.command.CommandResult;
import pl.zycienakodach.pragmaticflights.sdk.application.message.event.EventId;

import java.util.ArrayList;
import java.util.List;

public class RecordingCommandBus implements CommandBus {

  static record CommandEnvelope(Object command, CommandMetadata metadata) {
  }

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

  public Object lastExecutedCommand() {
    if (executedCommands.size() == 0) {
      return null;
    }
    var envelope = executedCommands.get(executedCommands.size() - 1);
    return envelope.command();
  }

  private CommandEnvelope lastExecutedCommandEnvelope() {
    if (executedCommands.size() == 0) {
      return null;
    }
    return executedCommands.get(executedCommands.size() - 1);
  }

  public List<Object> commandsCausedBy(EventId causationId) {
    return commandsCausedBy(new CausationId(causationId.raw()));
  }

  public List<Object> commandsCausedBy(CausationId causationId) {
    return this.executedCommands.stream()
        .filter(envelope -> envelope.metadata().causationId().equals(causationId))
        .map(CommandEnvelope::command)
        .toList();
  }

  public Object lastCommandCausedBy(EventId eventId) {
    final List<Object> commandCausedByEvent = this.executedCommands.stream()
        .filter(envelope -> envelope.metadata().causationId().equals(new CausationId(eventId.raw())))
        .map(CommandEnvelope::command)
        .toList();
    return commandCausedByEvent.isEmpty() ? null : commandCausedByEvent.get(commandCausedByEvent.size() - 1);
  }

}

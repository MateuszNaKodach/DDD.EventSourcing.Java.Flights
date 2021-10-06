package pl.zycienakodach.pragmaticflights.sdk.infrastructure.message.command;

import pl.zycienakodach.pragmaticflights.sdk.application.message.CausationId;
import pl.zycienakodach.pragmaticflights.sdk.application.message.event.EventId;

import java.util.List;

public interface RecordedCommands {

  List<CommandEnvelope> executedCommands();

  default CommandEnvelope lastExecutedCommandEnvelope() {
    if (executedCommands().size() == 0) {
      return null;
    }
    return executedCommands().get(executedCommands().size() - 1);
  }

  default List<Object> commandsCausedBy(EventId causationId) {
    return commandsCausedBy(new CausationId(causationId.raw()));
  }

  default List<Object> commandsCausedBy(CausationId causationId) {
    return this.executedCommands().stream()
        .filter(envelope -> envelope.metadata().causationId().equals(causationId))
        .map(CommandEnvelope::command)
        .toList();
  }

  default Object lastCommandCausedBy(EventId eventId) {
    final List<Object> commandCausedByEvent = this.executedCommands().stream()
        .filter(envelope -> envelope.metadata().causationId().equals(new CausationId(eventId.raw())))
        .map(CommandEnvelope::command)
        .toList();
    return commandCausedByEvent.isEmpty() ? null : commandCausedByEvent.get(commandCausedByEvent.size() - 1);
  }
}

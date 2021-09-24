package pl.zycienakodach.pragmaticflights.shared.application.message.command;

public interface CommandBus {

  <T> void registerHandler(Class<T> commandType, CommandHandler<T> handler);

  <T> CommandResult execute(T command, CommandMetadata metadata);
}

package pl.zycienakodach.pragmaticflights.shared.application;

public interface CommandBus {

  <T> void registerHandler(Class<T> commandType, CommandHandler<T> handler);

  <T> CommandResult execute(T command);
}

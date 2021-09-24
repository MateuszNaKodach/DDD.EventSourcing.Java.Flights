package pl.zycienakodach.pragmaticflights.shared.application.message.command;

import pl.zycienakodach.pragmaticflights.shared.application.message.command.CommandResult;

import java.util.function.Function;

@FunctionalInterface
public interface CommandHandler<T> extends Function<T, CommandResult> {

}

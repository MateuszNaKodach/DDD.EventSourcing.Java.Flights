package pl.zycienakodach.pragmaticflights.sdk.application.message.command;

import java.util.function.BiFunction;

@FunctionalInterface
public interface CommandHandler<T> extends BiFunction<T, CommandMetadata, CommandResult> {

}

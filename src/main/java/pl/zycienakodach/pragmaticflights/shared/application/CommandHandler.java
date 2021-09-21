package pl.zycienakodach.pragmaticflights.shared.application;

import java.util.function.Function;

@FunctionalInterface
public interface CommandHandler<T> extends Function<T, CommandResult> {

}

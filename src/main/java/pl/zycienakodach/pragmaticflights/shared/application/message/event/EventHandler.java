package pl.zycienakodach.pragmaticflights.shared.application.message.event;

import java.util.function.Consumer;

@FunctionalInterface
public interface EventHandler<T> extends Consumer<T> {

}

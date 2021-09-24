package pl.zycienakodach.pragmaticflights.shared.application.message.event;

import java.util.function.BiConsumer;

@FunctionalInterface
public interface EventHandler<T> extends BiConsumer<T, EventMetadata> {

}

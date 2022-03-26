package pl.zycienakodach.esflights.sdk.application.message.event;

import java.util.function.BiConsumer;

@FunctionalInterface
public interface EventHandler<T> extends BiConsumer<T, EventMetadata> {

}

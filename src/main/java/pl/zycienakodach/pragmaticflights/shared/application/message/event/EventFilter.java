package pl.zycienakodach.pragmaticflights.shared.application.message.event;

import java.util.function.BiPredicate;

@FunctionalInterface
public interface EventFilter<T> extends BiPredicate<T, EventMetadata> {

}

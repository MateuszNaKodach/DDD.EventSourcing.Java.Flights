package pl.zycienakodach.pragmaticflights.sdk;

import pl.zycienakodach.pragmaticflights.sdk.application.eventstream.EventStreamName;
import pl.zycienakodach.pragmaticflights.sdk.application.message.command.CommandHandler;
import pl.zycienakodach.pragmaticflights.sdk.application.message.command.CommandMetadata;
import pl.zycienakodach.pragmaticflights.sdk.application.message.event.EventFilter;
import pl.zycienakodach.pragmaticflights.sdk.application.message.event.EventHandler;
import pl.zycienakodach.pragmaticflights.sdk.domain.DomainLogic;

import java.time.Instant;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

interface ApplicationConfig {
  <E> Application when(Class<E> eventType, EventHandler<E> handler);

  <E> Application when(Class<E> eventType, Function<E, ?> command);

  <E> Application when(Class<E> eventType, EventHandler<E> handler, EventFilter<E> filter);

  <C, E> Application onCommand(Class<C> commandType, BiFunction<C, CommandMetadata, EventStreamName> streamName, Function<C, DomainLogic<E>> domainLogic);

  <C, E> Application onCommand(Class<C> commandType, BiFunction<C, CommandMetadata, EventStreamName> streamName, BiFunction<C, CommandMetadata, DomainLogic<E>> domainLogic);

  <C> Application onCommand(Class<C> commandType, CommandHandler<C> handler);

  Application withModule(ApplicationModule module);

  Application withModules(List<ApplicationModule> module);

  String generateId();

  Instant currentTime();
}

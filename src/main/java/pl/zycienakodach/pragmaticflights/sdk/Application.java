package pl.zycienakodach.pragmaticflights.sdk;

import pl.zycienakodach.pragmaticflights.sdk.application.eventstream.EventStreamName;
import pl.zycienakodach.pragmaticflights.sdk.application.message.command.CommandHandler;
import pl.zycienakodach.pragmaticflights.sdk.application.message.command.CommandMetadata;
import pl.zycienakodach.pragmaticflights.sdk.application.message.command.CommandResult;
import pl.zycienakodach.pragmaticflights.sdk.application.message.event.EventFilter;
import pl.zycienakodach.pragmaticflights.sdk.application.message.event.EventHandler;
import pl.zycienakodach.pragmaticflights.sdk.application.message.event.EventMetadata;
import pl.zycienakodach.pragmaticflights.sdk.domain.DomainLogic;

import java.time.Instant;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

public interface Application {
  <E> Application when(Class<E> eventType, EventHandler<E> handler);

  <E> Application when(Class<E> eventType, Function<E, ?> command);

  <E> Application when(Class<E> eventType, EventHandler<E> handler, EventFilter<E> filter);

  <C, E> Application onCommand(Class<C> commandType, BiFunction<C, CommandMetadata, EventStreamName> streamName, Function<C, DomainLogic<E>> domainLogic);

  <C, E> Application onCommand(Class<C> commandType, BiFunction<C, CommandMetadata, EventStreamName> streamName, BiFunction<C, CommandMetadata, DomainLogic<E>> domainLogic);

  <C> Application onCommand(Class<C> commandType, CommandHandler<C> handler);

  <T> Application execute(T command, ApplicationContext context);

  <T> CommandResult execute(T command, CommandMetadata metadata);

  Application withModule(ApplicationModule module);

  Application withModules(List<ApplicationModule> module);

  <E> void storeEvent(EventStreamName eventStream, E event, EventMetadata metadata);

  String generateId();

  Instant currentTime();
}

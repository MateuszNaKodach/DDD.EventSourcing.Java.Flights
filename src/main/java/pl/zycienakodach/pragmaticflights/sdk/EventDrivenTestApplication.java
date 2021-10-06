package pl.zycienakodach.pragmaticflights.sdk;

import pl.zycienakodach.pragmaticflights.sdk.application.eventstream.EventStreamName;
import pl.zycienakodach.pragmaticflights.sdk.application.message.CorrelationId;
import pl.zycienakodach.pragmaticflights.sdk.application.message.command.CommandHandler;
import pl.zycienakodach.pragmaticflights.sdk.application.message.command.CommandMetadata;
import pl.zycienakodach.pragmaticflights.sdk.application.message.command.CommandResult;
import pl.zycienakodach.pragmaticflights.sdk.application.message.event.EventFilter;
import pl.zycienakodach.pragmaticflights.sdk.application.message.event.EventHandler;
import pl.zycienakodach.pragmaticflights.sdk.application.message.event.EventId;
import pl.zycienakodach.pragmaticflights.sdk.application.message.event.EventMetadata;
import pl.zycienakodach.pragmaticflights.sdk.application.tenant.TenantId;
import pl.zycienakodach.pragmaticflights.sdk.domain.DomainLogic;

import java.time.Instant;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

public class EventDrivenTestApplication implements TestApplication {

  private final Application application;

  public EventDrivenTestApplication(Application application) {
    this.application = application;
  }

  public <E> Application when(Class<E> eventType, EventHandler<E> handler) {
    return application.when(eventType, handler);
  }

  public <E> Application when(Class<E> eventType, Function<E, ?> command) {
    return application.when(eventType, command);
  }

  public <E> Application when(Class<E> eventType, EventHandler<E> handler, EventFilter<E> filter) {
    return application.when(eventType, handler, filter);
  }

  public <C, E> Application onCommand(Class<C> commandType, BiFunction<C, CommandMetadata, EventStreamName> streamName, Function<C, DomainLogic<E>> domainLogic) {
    return application.onCommand(commandType, streamName, domainLogic);
  }

  public <C, E> Application onCommand(Class<C> commandType, BiFunction<C, CommandMetadata, EventStreamName> streamName, BiFunction<C, CommandMetadata, DomainLogic<E>> domainLogic) {
    return application.onCommand(commandType, streamName, domainLogic);
  }

  public <C> Application onCommand(Class<C> commandType, CommandHandler<C> handler) {
    return application.onCommand(commandType, handler);
  }

  public <T> CommandResult execute(T command, CommandMetadata metadata) {
    return application.execute(command, metadata);
  }

  public Application withModule(ApplicationModule module) {
    return application.withModule(module);
  }

  public Application withModules(List<ApplicationModule> modules) {
    return application.withModules(modules);
  }

  @Override
  public <E> EventMetadata eventOccurred(E event) {
    var allEventStream = EventStreamName.ofCategory("$").withId("all");
    return eventOccurred(allEventStream, event);
  }

  @Override
  public <E> EventMetadata eventOccurred(EventStreamName eventStream, E event) {
    final EventMetadata metadata = new EventMetadata(
        new EventId(application.generateId()),
        application.currentTime(),
        new TenantId("TestTenant"),
        new CorrelationId(application.generateId())
    );
    this.storeEvent(eventStream, event, metadata);
    return metadata;
  }

  @Override
  public <E> void storeEvent(EventStreamName eventStream, E event, EventMetadata metadata) {
    this.application.storeEvent(eventStream, event, metadata);
  }

  @Override
  public String generateId() {
    return application.generateId();
  }

  @Override
  public Instant currentTime() {
    return application.currentTime();
  }

  @Override
  public Application init() {
    return application.init();
  }
}

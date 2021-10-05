package pl.zycienakodach.pragmaticflights.sdk;

import pl.zycienakodach.pragmaticflights.sdk.application.service.ApplicationService;
import pl.zycienakodach.pragmaticflights.sdk.application.idgenerator.IdGenerator;
import pl.zycienakodach.pragmaticflights.sdk.application.eventstore.ExpectedStreamVersion;
import pl.zycienakodach.pragmaticflights.sdk.application.message.CausationId;
import pl.zycienakodach.pragmaticflights.sdk.application.message.CorrelationId;
import pl.zycienakodach.pragmaticflights.sdk.application.message.command.CommandBus;
import pl.zycienakodach.pragmaticflights.sdk.application.message.command.CommandHandler;
import pl.zycienakodach.pragmaticflights.sdk.application.message.command.CommandId;
import pl.zycienakodach.pragmaticflights.sdk.application.message.command.CommandMetadata;
import pl.zycienakodach.pragmaticflights.sdk.application.message.command.CommandResult;
import pl.zycienakodach.pragmaticflights.sdk.application.message.event.EventEnvelope;
import pl.zycienakodach.pragmaticflights.sdk.application.message.event.EventFilter;
import pl.zycienakodach.pragmaticflights.sdk.application.message.event.EventId;
import pl.zycienakodach.pragmaticflights.sdk.application.message.event.EventMetadata;
import pl.zycienakodach.pragmaticflights.sdk.application.tenant.TenantId;
import pl.zycienakodach.pragmaticflights.sdk.application.time.TimeProvider;
import pl.zycienakodach.pragmaticflights.sdk.domain.DomainLogic;
import pl.zycienakodach.pragmaticflights.sdk.application.message.event.EventHandler;
import pl.zycienakodach.pragmaticflights.sdk.application.eventstore.EventStore;
import pl.zycienakodach.pragmaticflights.sdk.application.eventstream.EventStreamName;

import java.time.Instant;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

public class Application {

  private final CommandBus commandBus;
  private final EventStore eventStore;
  private final ApplicationService applicationService;
  private final IdGenerator idGenerator;
  private final TimeProvider timeProvider;

  public Application(CommandBus commandBus, EventStore eventStore, ApplicationService applicationService, IdGenerator idGenerator, TimeProvider timeProvider) {
    this.commandBus = commandBus;
    this.eventStore = eventStore;
    this.applicationService = applicationService;
    this.idGenerator = idGenerator;
    this.timeProvider = timeProvider;
  }

  public <E> Application when(Class<E> eventType, EventHandler<E> handler) {
    this.eventStore.subscribe(eventType, handler);
    return this;
  }

  public <E> Application when(Class<E> eventType, Function<E, ?> command) {
    this.eventStore.subscribe(eventType, (e, m) -> {
      execute(command.apply(e), new CommandMetadata(
          new CommandId(this.generateId()),
          timeProvider.get(),
          m.tenantId(),
          m.correlationId(),
          new CausationId(m.eventId().raw())
      ));
    });
    return this;
  }

  public <E> Application when(Class<E> eventType, EventHandler<E> handler, EventFilter<E> filter) {
    this.eventStore.subscribe(eventType, (e, m) -> {
      if (filter.test(e, m)) {
        handler.accept(e, m);
      }
    });
    return this;
  }

  public <C, E> Application onCommand(Class<C> commandType, BiFunction<C, CommandMetadata, EventStreamName> streamName, Function<C, DomainLogic<E>> domainLogic) {
    this.commandBus.registerHandler(commandType, (c, m) -> this.applicationService.execute(streamName.apply(c, m), domainLogic.apply(c), m));
    return this;
  }

  public <C, E> Application onCommand(Class<C> commandType, BiFunction<C, CommandMetadata, EventStreamName> streamName, BiFunction<C, CommandMetadata, DomainLogic<E>> domainLogic) {
    this.commandBus.registerHandler(commandType, (c, m) -> this.applicationService.execute(streamName.apply(c, m), domainLogic.apply(c, m), m));
    return this;
  }

  public <C> Application onCommand(Class<C> commandType, CommandHandler<C> handler) {
    this.commandBus.registerHandler(commandType, handler);
    return this;
  }

  public <T> Application execute(T command, ApplicationContext context) {
    var commandId = new CommandId(generateId());
    this.commandBus.execute(command, new CommandMetadata(commandId, currentTime(), context.tenantId()));
    return this;
  }

  public <T> CommandResult execute(T command, CommandMetadata metadata) {
    return this.commandBus.execute(command, metadata);
  }

  public Application withModule(ApplicationModule module) {
    module.configure(this);
    return this;
  }

  public Application withModules(List<ApplicationModule> module) {
    module.forEach(m -> m.configure(this));
    return this;
  }

  private Instant currentTime() {
    return this.timeProvider.get();
  }

  private String generateId() {
    return this.idGenerator.get();
  }

  // todo: extract test methods
  public <E> EventMetadata testEventOccurred(E event) {
    var allEventStream = EventStreamName.ofCategory("$").withId("all");
    return testEventOccurred(allEventStream, event);
  }

  public <E> EventMetadata testEventOccurred(EventStreamName eventStream, E event) {
    final EventMetadata metadata = new EventMetadata(
        new EventId(generateId()),
        currentTime(),
        new TenantId("TestTenant"),
        new CorrelationId(generateId())
    );
    this.eventStore.write(eventStream, List.of(
        new EventEnvelope(event, metadata)
    ), new ExpectedStreamVersion.Any());
    return metadata;
  }

}

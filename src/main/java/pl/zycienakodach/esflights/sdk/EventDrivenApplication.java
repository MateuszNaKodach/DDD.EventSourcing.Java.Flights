package pl.zycienakodach.esflights.sdk;

import pl.zycienakodach.esflights.sdk.application.eventstore.EventStore;
import pl.zycienakodach.esflights.sdk.application.eventstore.ExpectedStreamVersion;
import pl.zycienakodach.esflights.sdk.application.eventstream.EventStreamName;
import pl.zycienakodach.esflights.sdk.application.idgenerator.IdGenerator;
import pl.zycienakodach.esflights.sdk.application.message.CausationId;
import pl.zycienakodach.esflights.sdk.application.message.CorrelationId;
import pl.zycienakodach.esflights.sdk.application.message.command.CommandBus;
import pl.zycienakodach.esflights.sdk.application.message.command.CommandHandler;
import pl.zycienakodach.esflights.sdk.application.message.command.CommandId;
import pl.zycienakodach.esflights.sdk.application.message.command.CommandMetadata;
import pl.zycienakodach.esflights.sdk.application.message.command.CommandResult;
import pl.zycienakodach.esflights.sdk.application.message.event.EventEnvelope;
import pl.zycienakodach.esflights.sdk.application.message.event.EventFilter;
import pl.zycienakodach.esflights.sdk.application.message.event.EventHandler;
import pl.zycienakodach.esflights.sdk.application.message.event.EventId;
import pl.zycienakodach.esflights.sdk.application.message.event.EventMetadata;
import pl.zycienakodach.esflights.sdk.application.service.ApplicationService;
import pl.zycienakodach.esflights.sdk.application.tenant.TenantId;
import pl.zycienakodach.esflights.sdk.application.time.TimeProvider;
import pl.zycienakodach.esflights.sdk.domain.DomainLogic;
import pl.zycienakodach.esflights.sdk.events.ApplicationInitialized;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

public class EventDrivenApplication implements Application {

  private final CommandBus commandBus;
  private final EventStore eventStore;
  private final ApplicationService applicationService;
  private final IdGenerator idGenerator;
  private final TimeProvider timeProvider;
  private final List<ApplicationModule> modules = new ArrayList<>();

  public EventDrivenApplication(CommandBus commandBus, EventStore eventStore, ApplicationService applicationService, IdGenerator idGenerator, TimeProvider timeProvider) {
    this.commandBus = commandBus;
    this.eventStore = eventStore;
    this.applicationService = applicationService;
    this.idGenerator = idGenerator;
    this.timeProvider = timeProvider;
  }

  @Override
  public <E> Application when(Class<E> eventType, EventHandler<E> handler) {
    this.eventStore.subscribe(eventType, handler);
    return this;
  }

  @Override
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

  @Override
  public <E> Application when(Class<E> eventType, EventHandler<E> handler, EventFilter<E> filter) {
    this.eventStore.subscribe(eventType, (e, m) -> {
      if (filter.test(e, m)) {
        handler.accept(e, m);
      }
    });
    return this;
  }

  @Override
  public <C, E> Application onCommand(Class<C> commandType, BiFunction<C, CommandMetadata, EventStreamName> streamName, Function<C, DomainLogic<E>> domainLogic) {
    this.commandBus.registerHandler(commandType, (c, m) -> this.applicationService.execute(streamName.apply(c, m), domainLogic.apply(c), m));
    return this;
  }

  @Override
  public <C, E> Application onCommand(Class<C> commandType, BiFunction<C, CommandMetadata, EventStreamName> streamName, BiFunction<C, CommandMetadata, DomainLogic<E>> domainLogic) {
    this.commandBus.registerHandler(commandType, (c, m) -> this.applicationService.execute(streamName.apply(c, m), domainLogic.apply(c, m), m));
    return this;
  }

  @Override
  public <C> Application onCommand(Class<C> commandType, CommandHandler<C> handler) {
    this.commandBus.registerHandler(commandType, handler);
    return this;
  }

  @Override
  public <T> CommandResult execute(T command, CommandMetadata metadata) {
    return this.commandBus.execute(command, metadata);
  }

  public <E> void storeEvent(EventStreamName eventStream, E event, EventMetadata metadata) {
    this.eventStore.write(eventStream, List.of(
        new EventEnvelope(event, metadata)
    ), new ExpectedStreamVersion.Any());
  }

  @Override
  public Application withModule(ApplicationModule module) {
    return withModules(List.of(module));
  }

  @Override
  public Application withModules(List<ApplicationModule> modules) {
    this.modules.addAll(modules);
    return this;
  }

  @Override
  public Instant currentTime() {
    return this.timeProvider.get();
  }

  @Override
  public String generateId() {
    return this.idGenerator.get();
  }

  @Override
  public Application init() {
    this.modules.forEach(m -> m.configure(this));
    var applicationInitialized = ApplicationInitialized.at(this.currentTime());
    var applicationEvents = EventStreamName.ofCategory("$").withId("application");
    var eventMetadata = new EventMetadata(
        new EventId(this.generateId()),
        timeProvider.get(),
        TenantId.undefined(),
        new CorrelationId(this.generateId()),
        new CausationId(this.generateId())
    );
    this.storeEvent(applicationEvents, applicationInitialized, eventMetadata);
    return this;
  }
}

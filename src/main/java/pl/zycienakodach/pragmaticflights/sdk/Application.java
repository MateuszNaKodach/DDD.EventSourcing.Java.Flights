package pl.zycienakodach.pragmaticflights.sdk;

import pl.zycienakodach.pragmaticflights.sdk.application.ApplicationService;
import pl.zycienakodach.pragmaticflights.sdk.application.IdGenerator;
import pl.zycienakodach.pragmaticflights.sdk.application.message.command.CommandBus;
import pl.zycienakodach.pragmaticflights.sdk.application.message.command.CommandHandler;
import pl.zycienakodach.pragmaticflights.sdk.application.message.command.CommandId;
import pl.zycienakodach.pragmaticflights.sdk.application.message.command.CommandMetadata;
import pl.zycienakodach.pragmaticflights.sdk.application.message.command.CommandResult;
import pl.zycienakodach.pragmaticflights.sdk.application.message.event.EventFilter;
import pl.zycienakodach.pragmaticflights.sdk.domain.DomainLogic;
import pl.zycienakodach.pragmaticflights.sdk.application.message.event.EventHandler;
import pl.zycienakodach.pragmaticflights.sdk.application.eventstore.EventStore;
import pl.zycienakodach.pragmaticflights.sdk.application.EventStreamName;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

public class Application {

  private final CommandBus commandBus;
  private final EventStore eventStore;
  private final ApplicationService applicationService;
  private final IdGenerator idGenerator;

  public Application(CommandBus commandBus, EventStore eventStore, ApplicationService applicationService, IdGenerator idGenerator) {
    this.commandBus = commandBus;
    this.eventStore = eventStore;
    this.applicationService = applicationService;
    this.idGenerator = idGenerator;
  }

  public <E> Application when(Class<E> eventType, EventHandler<E> handler) {
    this.eventStore.subscribe(eventType, handler);
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

  public <C> Application onCommand(Class<C> commandType, CommandHandler<C> handler) {
    this.commandBus.registerHandler(commandType, handler);
    return this;
  }

  public <T> Application execute(T command, ApplicationContext context) {
    var commandId = new CommandId(idGenerator.get());
    this.commandBus.execute(command, new CommandMetadata(commandId, context.tenantId()));
    return this;
  }

  public <T> CommandResult execute(T command, CommandMetadata metadata) {
    //var commandId = new CommandId(idGenerator.get());
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

  public String generateId() {
    return this.idGenerator.get();
  }

}

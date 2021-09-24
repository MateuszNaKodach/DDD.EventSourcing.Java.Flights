package pl.zycienakodach.pragmaticflights.shared;

import pl.zycienakodach.pragmaticflights.shared.application.ApplicationService;
import pl.zycienakodach.pragmaticflights.shared.application.IdGenerator;
import pl.zycienakodach.pragmaticflights.shared.application.message.command.CommandBus;
import pl.zycienakodach.pragmaticflights.shared.application.message.command.CommandHandler;
import pl.zycienakodach.pragmaticflights.shared.application.message.command.CommandId;
import pl.zycienakodach.pragmaticflights.shared.application.message.command.CommandMetadata;
import pl.zycienakodach.pragmaticflights.shared.application.tenant.TenantId;
import pl.zycienakodach.pragmaticflights.shared.domain.DomainLogic;
import pl.zycienakodach.pragmaticflights.shared.application.message.event.EventHandler;
import pl.zycienakodach.pragmaticflights.shared.application.eventstore.EventStore;
import pl.zycienakodach.pragmaticflights.shared.application.EventStreamName;
import pl.zycienakodach.pragmaticflights.shared.domain.DomainEvent;

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

  public <E> Application onEvent(Class<E> eventType, EventHandler<E> handler){
    this.eventStore.subscribe(eventType, handler);
    return this;
  }

  public <C, E extends DomainEvent> Application onCommand(Class<C> commandType, Function<C, EventStreamName> streamName, DomainLogic<E> domainLogic){
    this.commandBus.registerHandler(commandType, (c,m) -> this.applicationService.execute(streamName.apply(c), domainLogic, m));
    return this;
  }

  public <C> Application onCommand(Class<C> commandType, CommandHandler<C> handler){
    this.commandBus.registerHandler(commandType, handler);
    return this;
  }

  public <T> Application execute(T command, ApplicationContext context){
    var commandId = new CommandId(idGenerator.get());
    this.commandBus.execute(command, new CommandMetadata(commandId, context.tenantId()));
    return this;
  }

  public Application withModule(ApplicationModule module){
    module.configure(this);
    return this;
  }

}

package pl.zycienakodach.pragmaticflights.shared;

import pl.zycienakodach.pragmaticflights.shared.application.ApplicationService;
import pl.zycienakodach.pragmaticflights.shared.application.CommandBus;
import pl.zycienakodach.pragmaticflights.shared.application.CommandHandler;
import pl.zycienakodach.pragmaticflights.shared.application.DomainLogic;
import pl.zycienakodach.pragmaticflights.shared.application.EventHandler;
import pl.zycienakodach.pragmaticflights.shared.application.EventStore;
import pl.zycienakodach.pragmaticflights.shared.application.EventStreamName;
import pl.zycienakodach.pragmaticflights.shared.domain.DomainEvent;

import java.util.function.Function;

public class Application {

  private final CommandBus commandBus;
  private final EventStore eventStore;
  private final ApplicationService applicationService;

  public Application(CommandBus commandBus, EventStore eventStore, ApplicationService applicationService) {
    this.commandBus = commandBus;
    this.eventStore = eventStore;
    this.applicationService = applicationService;
  }

  public <E> Application onEvent(Class<E> eventType, EventHandler<E> handler){
    this.eventStore.subscribe(eventType, handler);
    return this;
  }

  public <C, E extends DomainEvent> Application onCommand(Class<C> commandType, Function<C, EventStreamName> streamName, DomainLogic<E> domainLogic){
    this.commandBus.registerHandler(commandType, c -> this.applicationService.execute(streamName.apply(c), domainLogic));
    return this;
  }

  public <C> Application onCommand(Class<C> commandType, CommandHandler<C> handler){
    this.commandBus.registerHandler(commandType, handler);
    return this;
  }

  public <T> Application execute(T command){
    this.commandBus.execute(command);
    return this;
  }

  public Application withModule(ApplicationModule module){
    module.configure(this);
    return this;
  }

}

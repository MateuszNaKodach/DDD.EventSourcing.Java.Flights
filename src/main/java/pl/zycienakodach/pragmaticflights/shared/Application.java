package pl.zycienakodach.pragmaticflights.shared;

import pl.zycienakodach.pragmaticflights.shared.application.ApplicationService;
import pl.zycienakodach.pragmaticflights.shared.application.CommandBus;
import pl.zycienakodach.pragmaticflights.shared.application.DomainLogic;
import pl.zycienakodach.pragmaticflights.shared.application.EventBus;
import pl.zycienakodach.pragmaticflights.shared.application.EventHandler;
import pl.zycienakodach.pragmaticflights.shared.application.EventSource;
import pl.zycienakodach.pragmaticflights.shared.application.EventStore;
import pl.zycienakodach.pragmaticflights.shared.application.EventStreamName;

import java.util.function.Consumer;

public class Application {

  private final CommandBus commandBus;
  private final EventStore eventStore;
  private final ApplicationService applicationService;

  public Application(CommandBus commandBus, EventStore eventStore) {
    this.commandBus = commandBus;
    this.eventStore = eventStore;
    this.applicationService = new ApplicationService(this.eventStore);
  }

  public <E> Application onEvent(Class<E> eventType, EventHandler<E> handler){
    this.eventStore.subscribe(eventType, handler);
    return this;
  }

  //overload onCommand to applicationService (just return events)
  public <C, E> Application onCommand(Class<C> commandType, EventStreamName streamName, DomainLogic<E> handler){
    this.commandBus.registerHandler(commandType, c -> this.applicationService.execute(streamName, handler));
    return this;
  }

  public <T> Application execute(T command){
    this.commandBus.execute(command);
    return this;
  }

}

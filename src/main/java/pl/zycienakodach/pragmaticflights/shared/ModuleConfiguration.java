package pl.zycienakodach.pragmaticflights.shared;

import pl.zycienakodach.pragmaticflights.shared.application.ApplicationService;
import pl.zycienakodach.pragmaticflights.shared.application.CommandBus;
import pl.zycienakodach.pragmaticflights.shared.application.EventSource;
import pl.zycienakodach.pragmaticflights.shared.application.EventStore;

import java.util.function.Consumer;

public class ModuleConfiguration {

  private final CommandBus commandBus;
  private final EventSource eventSource;

  public ModuleConfiguration(CommandBus commandBus, EventSource eventSource) {
    this.commandBus = commandBus;
    this.eventSource = eventSource;
  }

  public <T> ModuleConfiguration subscribe(Class<T> eventType, Consumer<T> handler){
    this.eventSource.subscribe(eventType, handler);
    return this;
  }

  //how to pass app context / applicationService
  public <T> ModuleConfiguration registerCommandHandler(Class<T> commandType, Consumer<T> handler){
    this.commandBus.registerHandler(commandType, handler);
    return this;
  }

//  public <T> ModuleConfiguration execute(T command){
//    this.commandBus.execute(command);
//    return this;
//  }

}

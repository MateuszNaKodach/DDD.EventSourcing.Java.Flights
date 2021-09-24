package pl.zycienakodach.pragmaticflights;

import pl.zycienakodach.pragmaticflights.shared.Application;
import pl.zycienakodach.pragmaticflights.shared.application.ApplicationService;
import pl.zycienakodach.pragmaticflights.shared.infrastructure.InMemoryCommandBus;
import pl.zycienakodach.pragmaticflights.shared.infrastructure.InMemoryEventBus;
import pl.zycienakodach.pragmaticflights.shared.infrastructure.InMemoryEventStore;

public class ApplicationTestFixtures {

  private ApplicationTestFixtures() {
  }

  public static Application inMemoryApplication() {
    var eventBus = new InMemoryEventBus();

    var commandBus = new InMemoryCommandBus();
    var eventStore = new InMemoryEventStore(eventBus);

    var applicationService = new ApplicationService(eventStore);
    return new Application(commandBus, eventStore, applicationService);
  }

}

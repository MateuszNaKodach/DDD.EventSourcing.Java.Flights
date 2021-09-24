package pl.zycienakodach.pragmaticflights;

import pl.zycienakodach.pragmaticflights.shared.Application;
import pl.zycienakodach.pragmaticflights.shared.application.IdGenerator;
import pl.zycienakodach.pragmaticflights.shared.application.time.TimeProvider;
import pl.zycienakodach.pragmaticflights.shared.infrastructure.EventStoreApplicationService;
import pl.zycienakodach.pragmaticflights.shared.infrastructure.InMemoryCommandBus;
import pl.zycienakodach.pragmaticflights.shared.infrastructure.InMemoryEventBus;
import pl.zycienakodach.pragmaticflights.shared.infrastructure.InMemoryEventStore;

import java.time.Clock;
import java.util.UUID;

public class ApplicationTestFixtures {

  private ApplicationTestFixtures() {
  }

  public static Application inMemoryApplication() {
    var eventBus = new InMemoryEventBus();

    var commandBus = new InMemoryCommandBus();
    var eventStore = new InMemoryEventStore(eventBus);
    IdGenerator idGenerator = () -> UUID.randomUUID().toString();
    var clock = Clock.systemUTC();
    TimeProvider timeProvider = clock::instant;

    var applicationService = new EventStoreApplicationService(eventStore, idGenerator, timeProvider);
    return new Application(commandBus, eventStore, applicationService, idGenerator);
  }

}

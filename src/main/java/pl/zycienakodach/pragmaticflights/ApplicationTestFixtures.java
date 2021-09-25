package pl.zycienakodach.pragmaticflights;

import pl.zycienakodach.pragmaticflights.shared.Application;
import pl.zycienakodach.pragmaticflights.shared.application.IdGenerator;
import pl.zycienakodach.pragmaticflights.shared.application.message.event.EventBus;
import pl.zycienakodach.pragmaticflights.shared.application.time.TimeProvider;
import pl.zycienakodach.pragmaticflights.shared.infrastructure.EventStoreApplicationService;
import pl.zycienakodach.pragmaticflights.shared.infrastructure.message.command.InMemoryCommandBus;
import pl.zycienakodach.pragmaticflights.shared.infrastructure.message.event.InMemoryEventBus;
import pl.zycienakodach.pragmaticflights.shared.infrastructure.eventstore.InMemoryEventStore;

import java.time.Clock;
import java.util.UUID;

public class ApplicationTestFixtures {

  private ApplicationTestFixtures() {
  }

  public static Application inMemoryApplication() {
    var eventBus = new InMemoryEventBus();
    return inMemoryApplication(eventBus);
  }

  public static Application inMemoryApplication(EventBus eventBus) {
    var commandBus = new InMemoryCommandBus();
    var eventStore = new InMemoryEventStore(eventBus);
    IdGenerator idGenerator = () -> UUID.randomUUID().toString();
    var clock = Clock.systemUTC();
    TimeProvider timeProvider = clock::instant;

    var applicationService = new EventStoreApplicationService(eventStore, idGenerator, timeProvider);
    return new Application(commandBus, eventStore, applicationService, idGenerator);
  }

}

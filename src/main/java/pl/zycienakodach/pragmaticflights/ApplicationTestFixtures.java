package pl.zycienakodach.pragmaticflights;

import pl.zycienakodach.pragmaticflights.sdk.Application;
import pl.zycienakodach.pragmaticflights.sdk.application.IdGenerator;
import pl.zycienakodach.pragmaticflights.sdk.application.message.event.EventBus;
import pl.zycienakodach.pragmaticflights.sdk.application.time.TimeProvider;
import pl.zycienakodach.pragmaticflights.sdk.infrastructure.EventStoreApplicationService;
import pl.zycienakodach.pragmaticflights.sdk.infrastructure.message.command.InMemoryCommandBus;
import pl.zycienakodach.pragmaticflights.sdk.infrastructure.message.event.InMemoryEventBus;
import pl.zycienakodach.pragmaticflights.sdk.infrastructure.eventstore.InMemoryEventStore;

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

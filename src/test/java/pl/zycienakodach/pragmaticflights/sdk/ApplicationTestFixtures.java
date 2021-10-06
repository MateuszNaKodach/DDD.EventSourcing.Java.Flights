package pl.zycienakodach.pragmaticflights.sdk;

import pl.zycienakodach.pragmaticflights.sdk.application.idgenerator.IdGenerator;
import pl.zycienakodach.pragmaticflights.sdk.application.message.command.CommandBus;
import pl.zycienakodach.pragmaticflights.sdk.application.message.event.EventBus;
import pl.zycienakodach.pragmaticflights.sdk.application.time.TimeProvider;
import pl.zycienakodach.pragmaticflights.sdk.infrastructure.eventstore.InMemoryEventStore;
import pl.zycienakodach.pragmaticflights.sdk.infrastructure.message.command.InMemoryCommandBus;
import pl.zycienakodach.pragmaticflights.sdk.infrastructure.message.command.RecordingCommandBus;
import pl.zycienakodach.pragmaticflights.sdk.infrastructure.message.event.InMemoryEventBus;
import pl.zycienakodach.pragmaticflights.sdk.infrastructure.message.event.RecordingEventBus;
import pl.zycienakodach.pragmaticflights.sdk.infrastructure.service.EventStoreApplicationService;

import java.time.Clock;
import java.util.Arrays;
import java.util.UUID;

public class ApplicationTestFixtures {

  private ApplicationTestFixtures() {
  }

  public static TestApplication inMemoryTestApplication(ApplicationModule... modules) {
    var commandBus = new RecordingCommandBus(new InMemoryCommandBus());
    var eventBus = new RecordingEventBus(new InMemoryEventBus());
    var application = inMemoryApplication(eventBus, commandBus);
    var app = new EventDrivenTestApplication(application, eventBus, commandBus);
    final var modulesList = Arrays.stream(modules).toList();
    app.withModules(modulesList);
    app.init();
    return app;
  }

  public static Application inMemoryApplication() {
    var eventBus = new InMemoryEventBus();
    return inMemoryApplication(eventBus);
  }

  public static Application inMemoryApplication(CommandBus commandBus) {
    return inMemoryApplication(new InMemoryEventBus(), commandBus);
  }

  public static Application inMemoryApplication(EventBus eventBus) {
    return inMemoryApplication(eventBus, new InMemoryCommandBus());
  }

  public static Application inMemoryApplication(EventBus eventBus, CommandBus commandBus) {
    var eventStore = new InMemoryEventStore(eventBus);
    IdGenerator idGenerator = () -> UUID.randomUUID().toString();
    var clock = Clock.systemUTC();
    TimeProvider timeProvider = clock::instant;

    var applicationService = new EventStoreApplicationService(eventStore, idGenerator, timeProvider);
    return new EventDrivenApplication(commandBus, eventStore, applicationService, idGenerator, timeProvider);
  }

}

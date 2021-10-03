package pl.zycienakodach.pragmaticflights.sdk.infrastructure.eventstore;


import pl.zycienakodach.pragmaticflights.sdk.infrastructure.message.event.InMemoryEventBus;

public class EventStoreTestFixtures {

  private EventStoreTestFixtures() {

  }

  public static InMemoryEventStore inMemoryEventStore() {
    return new InMemoryEventStore(new InMemoryEventBus());
  }
}

package pl.zycienakodach.esflights.sdk.infrastructure.eventstore;


import pl.zycienakodach.esflights.sdk.infrastructure.message.event.InMemoryEventBus;

public class EventStoreTestFixtures {

  private EventStoreTestFixtures() {

  }

  public static InMemoryEventStore inMemoryEventStore() {
    return new InMemoryEventStore(new InMemoryEventBus());
  }
}

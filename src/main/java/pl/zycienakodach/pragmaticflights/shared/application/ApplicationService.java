package pl.zycienakodach.pragmaticflights.shared.application;

import pl.zycienakodach.pragmaticflights.shared.domain.DomainEvent;

import java.util.List;

public class ApplicationService {

  private final EventStore eventStore;

  public ApplicationService(EventStore eventStore) {
    this.eventStore = eventStore;
  }

  public <EventType extends DomainEvent> CommandResult execute(
      EventStreamName streamName,
      DomainLogic<EventType> domainLogic
  ) {
    var eventStream = eventStore.read(streamName);

    //noinspection unchecked
    var previousDomainEvents = eventStream.events().stream().map(event -> (EventType) event).toList();
    var eventsToStore = domainLogic.apply(previousDomainEvents);

    var domainLogicResult = eventsToStore
        .peek(events -> {
          final List<DomainEvent> newEvents = events.stream().map(e -> (DomainEvent) e).toList();
          eventStore.write(streamName, newEvents, new ExpectedStreamVersion.Exactly(eventStream.version()));
        })
        .peekLeft(events -> {
          final List<DomainEvent> newEvents = events.stream().map(e -> (DomainEvent) e).toList();
          eventStore.write(streamName, newEvents, new ExpectedStreamVersion.Exactly(eventStream.version()));
        });

    return domainLogicResult.isRight()
        ? new CommandResult.Accepted()
        : new CommandResult.Rejected();
  }

}

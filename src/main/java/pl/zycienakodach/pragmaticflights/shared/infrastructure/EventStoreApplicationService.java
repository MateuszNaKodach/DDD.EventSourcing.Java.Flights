package pl.zycienakodach.pragmaticflights.shared.infrastructure;

import pl.zycienakodach.pragmaticflights.shared.application.ApplicationService;
import pl.zycienakodach.pragmaticflights.shared.application.EventStreamName;
import pl.zycienakodach.pragmaticflights.shared.application.eventstore.ExpectedStreamVersion;
import pl.zycienakodach.pragmaticflights.shared.application.eventstore.EventStore;
import pl.zycienakodach.pragmaticflights.shared.application.message.command.CommandResult;
import pl.zycienakodach.pragmaticflights.shared.domain.DomainEvent;
import pl.zycienakodach.pragmaticflights.shared.domain.DomainLogic;

import java.util.List;

public class EventStoreApplicationService implements ApplicationService {

  private final EventStore eventStore;

  public EventStoreApplicationService(EventStore eventStore) {
    this.eventStore = eventStore;
  }

  @Override
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

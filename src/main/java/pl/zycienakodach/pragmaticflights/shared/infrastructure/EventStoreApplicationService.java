package pl.zycienakodach.pragmaticflights.shared.infrastructure;

import pl.zycienakodach.pragmaticflights.shared.application.ApplicationService;
import pl.zycienakodach.pragmaticflights.shared.application.EventStream;
import pl.zycienakodach.pragmaticflights.shared.application.EventStreamName;
import pl.zycienakodach.pragmaticflights.shared.application.IdGenerator;
import pl.zycienakodach.pragmaticflights.shared.application.eventstore.ExpectedStreamVersion;
import pl.zycienakodach.pragmaticflights.shared.application.eventstore.EventStore;
import pl.zycienakodach.pragmaticflights.shared.application.message.CausationId;
import pl.zycienakodach.pragmaticflights.shared.application.message.command.CommandMetadata;
import pl.zycienakodach.pragmaticflights.shared.application.message.command.CommandResult;
import pl.zycienakodach.pragmaticflights.shared.application.message.event.EventEnvelope;
import pl.zycienakodach.pragmaticflights.shared.application.message.event.EventId;
import pl.zycienakodach.pragmaticflights.shared.application.message.event.EventMetadata;
import pl.zycienakodach.pragmaticflights.shared.application.time.TimeProvider;
import pl.zycienakodach.pragmaticflights.shared.domain.event.DomainEvent;
import pl.zycienakodach.pragmaticflights.shared.domain.DomainLogic;

import java.util.List;

public class EventStoreApplicationService implements ApplicationService {

  private final EventStore eventStore;
  private final IdGenerator idGenerator;
  private final TimeProvider timeProvider;

  public EventStoreApplicationService(EventStore eventStore, IdGenerator idGenerator, TimeProvider timeProvider) {
    this.eventStore = eventStore;
    this.idGenerator = idGenerator;
    this.timeProvider = timeProvider;
  }

  @Override
  public <EventType extends DomainEvent> CommandResult execute(
      EventStreamName streamName,
      DomainLogic<EventType> domainLogic,
      CommandMetadata metadata
  ) {
    var eventStream = eventStore.read(streamName);

    //noinspection unchecked
    var previousDomainEvents = eventStream.events().stream().map(event -> (EventType) event).toList();

    try {
      return executeDomainLogic(streamName, domainLogic, metadata, eventStream, previousDomainEvents);
    } catch (Exception e) {
      return new CommandResult.Rejected(e.getMessage());
    }
  }

  private <EventType extends DomainEvent> CommandResult executeDomainLogic(
      EventStreamName streamName,
      DomainLogic<EventType> domainLogic,
      CommandMetadata metadata,
      EventStream eventStream,
      List<EventType> previousDomainEvents
  ) {
    var eventsToStore = domainLogic.apply(previousDomainEvents);

    var domainLogicResult = eventsToStore
        .peek(events -> {
          final List<EventEnvelope> newEvents = events.stream()
              .map(e -> new EventEnvelope(e, new EventMetadata(new EventId(idGenerator.get()), timeProvider.get(), metadata.tenantId(), metadata.correlationId(), new CausationId(metadata.commandId().raw()))))
              .toList();
          eventStore.write(streamName, newEvents, new ExpectedStreamVersion.Exactly(eventStream.version()));
        })
        .peekLeft(events -> {
          final List<EventEnvelope> newEvents = events.stream()
              .map(e -> new EventEnvelope(e, new EventMetadata(new EventId(idGenerator.get()), timeProvider.get(), metadata.tenantId(), metadata.correlationId(), new CausationId(metadata.commandId().raw()))))
              .toList();
          eventStore.write(streamName, newEvents, new ExpectedStreamVersion.Exactly(eventStream.version()));
        });

    return domainLogicResult.isRight()
        ? new CommandResult.Accepted()
        : new CommandResult.Rejected();
  }

}

package pl.zycienakodach.esflights.sdk.infrastructure.service;

import pl.zycienakodach.esflights.sdk.application.service.ApplicationService;
import pl.zycienakodach.esflights.sdk.application.eventstream.EventStream;
import pl.zycienakodach.esflights.sdk.application.eventstream.EventStreamName;
import pl.zycienakodach.esflights.sdk.application.idgenerator.IdGenerator;
import pl.zycienakodach.esflights.sdk.application.eventstore.ExpectedStreamVersion;
import pl.zycienakodach.esflights.sdk.application.eventstore.EventStore;
import pl.zycienakodach.esflights.sdk.application.message.CausationId;
import pl.zycienakodach.esflights.sdk.application.message.command.CommandMetadata;
import pl.zycienakodach.esflights.sdk.application.message.command.CommandResult;
import pl.zycienakodach.esflights.sdk.application.message.event.EventEnvelope;
import pl.zycienakodach.esflights.sdk.application.message.event.EventId;
import pl.zycienakodach.esflights.sdk.application.message.event.EventMetadata;
import pl.zycienakodach.esflights.sdk.application.time.TimeProvider;
import pl.zycienakodach.esflights.sdk.domain.DomainLogic;

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
  public <EventType> CommandResult execute(
      EventStreamName streamName,
      DomainLogic<EventType> domainLogic,
      CommandMetadata metadata
  ) {
    final EventStreamName tenantStreamName = streamName.withCategoryPrefix(metadata.tenantId().raw());
    var eventStream = eventStore.read(tenantStreamName);

    //noinspection unchecked
    var previousDomainEvents = eventStream.events().stream().map(event -> (EventType) event).toList();

    try {
      return executeDomainLogic(tenantStreamName, domainLogic, metadata, eventStream, previousDomainEvents);
    } catch (Exception e) {
      return new CommandResult.Rejected(e.getMessage());
    }
  }

  private <EventType> CommandResult executeDomainLogic(
      EventStreamName streamName,
      DomainLogic<EventType> domainLogic,
      CommandMetadata metadata,
      EventStream eventStream,
      List<EventType> previousDomainEvents
  ) {
    var eventsToStore = domainLogic.apply(previousDomainEvents);

    var domainLogicResult = eventsToStore
        .stream()
        .map(e -> new EventEnvelope(e, new EventMetadata(new EventId(idGenerator.get()), timeProvider.get(), metadata.tenantId(), metadata.correlationId(), new CausationId(metadata.commandId().raw()))))
        .toList();
    eventStore.write(streamName, domainLogicResult, new ExpectedStreamVersion.Exactly(eventStream.version()));

    return new CommandResult.Accepted();
  }

}

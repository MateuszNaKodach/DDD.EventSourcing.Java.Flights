package pl.zycienakodach.pragmaticflights.sdk.infrastructure;

import pl.zycienakodach.pragmaticflights.sdk.application.ApplicationService;
import pl.zycienakodach.pragmaticflights.sdk.application.EventStream;
import pl.zycienakodach.pragmaticflights.sdk.application.EventStreamName;
import pl.zycienakodach.pragmaticflights.sdk.application.IdGenerator;
import pl.zycienakodach.pragmaticflights.sdk.application.eventstore.ExpectedStreamVersion;
import pl.zycienakodach.pragmaticflights.sdk.application.eventstore.EventStore;
import pl.zycienakodach.pragmaticflights.sdk.application.message.CausationId;
import pl.zycienakodach.pragmaticflights.sdk.application.message.command.CommandMetadata;
import pl.zycienakodach.pragmaticflights.sdk.application.message.command.CommandResult;
import pl.zycienakodach.pragmaticflights.sdk.application.message.event.EventEnvelope;
import pl.zycienakodach.pragmaticflights.sdk.application.message.event.EventId;
import pl.zycienakodach.pragmaticflights.sdk.application.message.event.EventMetadata;
import pl.zycienakodach.pragmaticflights.sdk.application.time.TimeProvider;
import pl.zycienakodach.pragmaticflights.sdk.domain.DomainLogic;

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

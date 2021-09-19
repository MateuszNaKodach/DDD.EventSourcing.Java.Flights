package pl.zycienakodach.pragmaticflights.shared.application;

import io.vavr.control.Either;
import pl.zycienakodach.pragmaticflights.shared.domain.DomainEvent;
import pl.zycienakodach.pragmaticflights.shared.domain.FailureDomainEvent;
import pl.zycienakodach.pragmaticflights.shared.domain.SuccessDomainEvent;

import java.util.List;
import java.util.function.Function;

public record ApplicationService(EventStore eventStore) {

  <EventType extends DomainEvent> CommandResult execute(EventStreamName streamName, Function<List<EventType>, Either<List<FailureDomainEvent>, List<SuccessDomainEvent>>> domainLogic) {
    var eventStream = eventStore.read(streamName);

    //noinspection unchecked
    var previousDomainEvents = eventStream.events().stream().map(event -> (EventType) event).toList();
    var eventsToStore = domainLogic.apply(previousDomainEvents);

    var domainLogicResult = eventsToStore
        .peek(events -> eventStore.write(streamName, events.stream().map(e -> (DomainEvent) e).toList(), new ExpectedStreamVersion.Exactly(eventStream.version())))
        .peekLeft(events -> eventStore.write(streamName, events.stream().map(e -> (DomainEvent) e).toList(), new ExpectedStreamVersion.Exactly(eventStream.version())));

    return domainLogicResult.isRight()
        ? new CommandResult.Accepted()
        : new CommandResult.Rejected();
  }

}

package pl.zycienakodach.pragmaticflights.shared.application;

import pl.zycienakodach.pragmaticflights.shared.application.message.command.CommandMetadata;
import pl.zycienakodach.pragmaticflights.shared.application.message.command.CommandResult;
import pl.zycienakodach.pragmaticflights.shared.domain.DomainEvent;
import pl.zycienakodach.pragmaticflights.shared.domain.DomainLogic;

public interface ApplicationService {
  <EventType extends DomainEvent> CommandResult execute(
      EventStreamName streamName,
      DomainLogic<EventType> domainLogic,
      CommandMetadata metadata
  );
}

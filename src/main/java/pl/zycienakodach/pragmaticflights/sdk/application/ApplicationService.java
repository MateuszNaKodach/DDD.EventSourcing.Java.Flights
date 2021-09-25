package pl.zycienakodach.pragmaticflights.sdk.application;

import pl.zycienakodach.pragmaticflights.sdk.application.message.command.CommandMetadata;
import pl.zycienakodach.pragmaticflights.sdk.application.message.command.CommandResult;
import pl.zycienakodach.pragmaticflights.sdk.domain.DomainLogic;

public interface ApplicationService {
  <EventType> CommandResult execute(
      EventStreamName streamName,
      DomainLogic<EventType> domainLogic,
      CommandMetadata metadata
  );
}

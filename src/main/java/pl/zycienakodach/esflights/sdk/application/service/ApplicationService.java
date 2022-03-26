package pl.zycienakodach.esflights.sdk.application.service;

import pl.zycienakodach.esflights.sdk.application.eventstream.EventStreamName;
import pl.zycienakodach.esflights.sdk.application.message.command.CommandMetadata;
import pl.zycienakodach.esflights.sdk.application.message.command.CommandResult;
import pl.zycienakodach.esflights.sdk.domain.DomainLogic;

public interface ApplicationService {
  <EventType> CommandResult execute(
      EventStreamName streamName,
      DomainLogic<EventType> domainLogic,
      CommandMetadata metadata
  );
}

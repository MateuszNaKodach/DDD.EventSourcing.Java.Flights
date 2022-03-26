package pl.zycienakodach.esflights.sdk;

import pl.zycienakodach.esflights.sdk.application.eventstream.EventStreamName;
import pl.zycienakodach.esflights.sdk.application.message.event.EventMetadata;
import pl.zycienakodach.esflights.sdk.infrastructure.message.command.RecordedCommands;
import pl.zycienakodach.esflights.sdk.infrastructure.message.event.RecordedEvents;

public interface TestApplication extends Application, RecordedEvents, RecordedCommands {

  <E> EventMetadata eventOccurred(E event);

  <E> EventMetadata eventOccurred(EventStreamName eventStream, E event);
}

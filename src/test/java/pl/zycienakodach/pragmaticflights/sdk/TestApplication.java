package pl.zycienakodach.pragmaticflights.sdk;

import pl.zycienakodach.pragmaticflights.sdk.application.eventstream.EventStreamName;
import pl.zycienakodach.pragmaticflights.sdk.application.message.event.EventMetadata;
import pl.zycienakodach.pragmaticflights.sdk.infrastructure.message.command.RecordedCommands;
import pl.zycienakodach.pragmaticflights.sdk.infrastructure.message.event.RecordedEvents;

public interface TestApplication extends Application, RecordedEvents, RecordedCommands {

  <E> EventMetadata eventOccurred(E event);

  <E> EventMetadata eventOccurred(EventStreamName eventStream, E event);
}

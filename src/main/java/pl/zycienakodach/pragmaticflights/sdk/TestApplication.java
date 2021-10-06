package pl.zycienakodach.pragmaticflights.sdk;

import pl.zycienakodach.pragmaticflights.sdk.application.eventstream.EventStreamName;
import pl.zycienakodach.pragmaticflights.sdk.application.message.event.EventMetadata;

public interface TestApplication extends Application {

  <E> EventMetadata eventOccurred(E event);

  <E> EventMetadata eventOccurred(EventStreamName eventStream, E event);
}

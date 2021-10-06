package pl.zycienakodach.pragmaticflights.sdk;

import pl.zycienakodach.pragmaticflights.sdk.application.eventstream.EventStreamName;
import pl.zycienakodach.pragmaticflights.sdk.application.message.event.EventMetadata;

public interface TestApplication extends Application {

  <E> EventMetadata testEventOccurred(E event);

  <E> EventMetadata testEventOccurred(EventStreamName eventStream, E event);
}

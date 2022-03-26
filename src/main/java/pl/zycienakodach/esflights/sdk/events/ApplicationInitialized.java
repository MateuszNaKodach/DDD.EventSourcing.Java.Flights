package pl.zycienakodach.esflights.sdk.events;

import java.time.Instant;

public class ApplicationInitialized {

  private final Instant timestamp;

  private ApplicationInitialized(Instant timestamp) {
    this.timestamp = timestamp;
  }

  public static ApplicationInitialized at(Instant timestamp) {
    return new ApplicationInitialized(timestamp);
  }

  Instant timestamp() {
    return timestamp;
  }
}

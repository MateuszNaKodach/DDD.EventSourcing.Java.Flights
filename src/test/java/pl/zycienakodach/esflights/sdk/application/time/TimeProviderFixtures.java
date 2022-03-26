package pl.zycienakodach.esflights.sdk.application.time;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class TimeProviderFixtures {

  public static TimeProvider anyTime() {
    return () -> LocalDateTime.now().toInstant(ZoneOffset.UTC);
  }

  public static TimeProvider isUtcMidnightOf(int year, int month, int dayOfMonth) {
    return () -> utcMidnightOf(year, month, dayOfMonth);
  }

  public static Instant utcMidnightOf(int year, int month, int dayOfMonth) {
    return LocalDateTime.of(year, month, dayOfMonth, 0, 0).toInstant(ZoneOffset.UTC);
  }

  public static Instant utc12h00mOf(int year, int month, int dayOfMonth) {
    return LocalDateTime.of(year, month, dayOfMonth, 12, 0).toInstant(ZoneOffset.UTC);
  }

}

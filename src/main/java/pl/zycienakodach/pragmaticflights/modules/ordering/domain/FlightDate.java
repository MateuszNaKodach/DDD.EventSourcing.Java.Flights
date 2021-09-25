package pl.zycienakodach.pragmaticflights.modules.ordering.domain;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;
import java.util.function.Supplier;

final class FlightDate {
  private final LocalDate raw;

  public FlightDate(LocalDate raw, Supplier<Instant> currentTime) {
    //if(currentTime.get().)
    this.raw = raw;
  }

  public LocalDate raw() {
    return raw;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) return true;
    if (obj == null || obj.getClass() != this.getClass()) return false;
    var that = (FlightDate) obj;
    return Objects.equals(this.raw, that.raw);
  }

  @Override
  public int hashCode() {
    return Objects.hash(raw);
  }

  @Override
  public String toString() {
    return "FlightDate[" +
        "raw=" + raw + ']';
  }

}

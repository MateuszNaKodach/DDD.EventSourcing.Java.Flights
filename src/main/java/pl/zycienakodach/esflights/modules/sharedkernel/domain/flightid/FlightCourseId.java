package pl.zycienakodach.esflights.modules.sharedkernel.domain.flightid;

import java.time.Instant;

public record FlightCourseId(FlightId flightId, Instant departureAt) {

  public static FlightCourseId of(FlightId flightId, Instant departureAt) {
    return new FlightCourseId(flightId, departureAt);
  }

  public static FlightCourseId fromRaw(String raw) {
    var flightCourseIdParts = raw.split(" \\+ ");
    return new FlightCourseId(FlightId.fromRaw(flightCourseIdParts[0]), Instant.parse(flightCourseIdParts[1]));
  }

  public String raw() {
    return flightId.raw() + " + " + departureAt.toString();
  }

}

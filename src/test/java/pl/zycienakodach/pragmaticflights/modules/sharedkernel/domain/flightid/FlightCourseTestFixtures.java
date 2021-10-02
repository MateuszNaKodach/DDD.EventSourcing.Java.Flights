package pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.flightid;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class FlightCourseTestFixtures {

  public static String rawFlightCourseId() {
    return rawFlightCourseId(flightDepartureDate());
  }

  public static Instant flightDepartureDate() {
    return LocalDateTime.now().toInstant(ZoneOffset.UTC);
  }

  public static String rawFlightCourseId(Instant departureAt) {
    return aFlightCourseId(departureAt).raw();
  }

  public static String rawFlightCourseId(String rawFlightId, Instant departureAt) {
    return aFlightCourseId(rawFlightId, departureAt).raw();
  }

  public static FlightCourseId aFlightCourseId(Instant departureAt) {
    return FlightCourseId.fromRaw("KLM 12345 ABC + " + departureAt);
  }

  public static FlightCourseId aFlightCourseId(String rawFlightId, Instant departureAt) {
    return FlightCourseId.fromRaw(rawFlightId + " + " + departureAt);
  }
}

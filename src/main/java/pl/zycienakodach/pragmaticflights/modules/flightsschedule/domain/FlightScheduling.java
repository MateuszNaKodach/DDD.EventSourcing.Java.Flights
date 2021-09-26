package pl.zycienakodach.pragmaticflights.modules.flightsschedule.domain;

import pl.zycienakodach.pragmaticflights.modules.flightsschedule.api.FlightScheduleEvent;
import pl.zycienakodach.pragmaticflights.modules.flightsschedule.api.FlightScheduled;
import pl.zycienakodach.pragmaticflights.sdk.domain.DomainLogic;
import pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.flightid.FlightId;
import pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.iata.IATAAirportCode;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

public class FlightScheduling {

  private FlightScheduling() {
  }

  public static DomainLogic<FlightScheduleEvent> scheduleFlight(
      FlightId flightId,
      IATAAirportCode origin,
      IATAAirportCode destination,
      LocalTime departureTime,
      Set<DayOfWeek> departureDays
  ) {
    return (List<FlightScheduleEvent> pastEvents) -> {
      if (departureDays.isEmpty()) {
        throw new IllegalArgumentException("Flight must have at least one departure day.");
      }
      var flightScheduled = new FlightScheduled(
          flightId.raw(),
          origin.raw(),
          destination.raw(),
          departureTime,
          departureDays
      );
      return List.of(flightScheduled);
    };
  }
}

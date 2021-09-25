package pl.zycienakodach.pragmaticflights.flightsschedule.domain;

import pl.zycienakodach.pragmaticflights.flightsschedule.api.FlightScheduleDomainEvent;
import pl.zycienakodach.pragmaticflights.flightsschedule.api.FlightScheduled;
import pl.zycienakodach.pragmaticflights.shared.domain.DomainLogic;
import pl.zycienakodach.pragmaticflights.sharedkernel.domain.flightid.FlightId;
import pl.zycienakodach.pragmaticflights.sharedkernel.domain.iata.IATAAirportCode;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

public class FlightScheduling {

  private FlightScheduling() {
  }

  public static DomainLogic<FlightScheduleDomainEvent> scheduleFlight(
      FlightId flightId,
      IATAAirportCode origin,
      IATAAirportCode destination,
      LocalTime departureTime,
      Set<DayOfWeek> departureDays
  ) {
    return (List<FlightScheduleDomainEvent> pastEvents) -> {
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

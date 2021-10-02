package pl.zycienakodach.pragmaticflights.modules.flightsschedule.domain;

import pl.zycienakodach.pragmaticflights.modules.flightsschedule.api.events.FlightScheduleEvent;
import pl.zycienakodach.pragmaticflights.modules.flightsschedule.api.events.FlightScheduled;
import pl.zycienakodach.pragmaticflights.sdk.domain.DomainLogic;
import pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.flightid.FlightId;
import pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.iata.IATAAirportCode;
import pl.zycienakodach.pragmaticflights.sdk.domain.LocalDateRange;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
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
      Set<DayOfWeek> departureDays,
      LocalDateRange inDateRange
  ) {
    return (List<FlightScheduleEvent> pastEvents) -> {
      if (departureDays.isEmpty()) {
        throw new IllegalArgumentException("Flight must have at least one departure day.");
      }
      // todo: check if already scheduled
      return inDateRange
          .datesInRange()
          .stream()
          .filter(date -> departureDays.contains(date.getDayOfWeek()))
          .map(date -> new FlightScheduled(
              flightId.raw(),
              origin.raw(),
              destination.raw(),
              ZonedDateTime.of(LocalDateTime.of(date, departureTime), ZoneId.of("UTC")).toInstant()
          ))
          .map(event -> (FlightScheduleEvent) event)
          .toList();
    };
  }
}

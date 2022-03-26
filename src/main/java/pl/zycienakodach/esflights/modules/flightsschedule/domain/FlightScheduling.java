package pl.zycienakodach.esflights.modules.flightsschedule.domain;

import pl.zycienakodach.esflights.modules.flightsschedule.api.events.FlightScheduleEvent;
import pl.zycienakodach.esflights.modules.flightsschedule.api.events.FlightCourseScheduled;
import pl.zycienakodach.esflights.modules.sharedkernel.domain.flightid.FlightCourseId;
import pl.zycienakodach.esflights.sdk.domain.DomainLogic;
import pl.zycienakodach.esflights.modules.sharedkernel.domain.flightid.FlightId;
import pl.zycienakodach.esflights.modules.sharedkernel.domain.iata.IATAAirportCode;
import pl.zycienakodach.esflights.sdk.domain.LocalDateRange;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;

public class FlightScheduling {

  private FlightScheduling() {
  }

  public static DomainLogic<FlightScheduleEvent> scheduleFlightCourses(
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
          .map(date -> {
            final Instant departureAt = ZonedDateTime.of(LocalDateTime.of(date, departureTime), ZoneId.of("UTC")).toInstant();
            return new FlightCourseScheduled(
                FlightCourseId.of(flightId, departureAt).raw(),
                flightId.raw(),
                origin.raw(),
                destination.raw(),
                departureAt
            );
          })
          .map(event -> (FlightScheduleEvent) event)
          .toList();
    };
  }
}

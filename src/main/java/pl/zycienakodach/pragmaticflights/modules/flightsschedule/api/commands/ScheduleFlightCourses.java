package pl.zycienakodach.pragmaticflights.modules.flightsschedule.api.commands;

import pl.zycienakodach.pragmaticflights.modules.flightsschedule.api.events.FlightScheduleEvent;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

public record ScheduleFlightCourses(
    String flightId,
    String origin,
    String destination,
    LocalTime departureTime,
    Set<DayOfWeek> departureDays,
    LocalDate fromDate,
    LocalDate toDate
) implements FlightScheduleEvent {
}

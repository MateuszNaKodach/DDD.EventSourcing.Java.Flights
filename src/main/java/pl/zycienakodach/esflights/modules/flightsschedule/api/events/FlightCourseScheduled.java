package pl.zycienakodach.esflights.modules.flightsschedule.api.events;

import java.time.Instant;

public record FlightCourseScheduled(
    String flightCourseId,
    String flightId,
    String origin,
    String destination,
    Instant departureAt
) implements FlightScheduleEvent {
}

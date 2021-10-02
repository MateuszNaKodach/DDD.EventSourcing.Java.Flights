package pl.zycienakodach.pragmaticflights.modules.flightsschedule.api.events;

import java.time.Instant;

public record FlightScheduled(
    String flightId,
    String origin,
    String destination,
    Instant departureAt
) implements FlightScheduleEvent {
}

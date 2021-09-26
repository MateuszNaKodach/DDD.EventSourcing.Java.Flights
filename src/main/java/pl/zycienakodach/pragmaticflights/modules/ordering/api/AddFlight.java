package pl.zycienakodach.pragmaticflights.modules.ordering.api;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Set;

record AddFlight(
    String flightId,
    String origin,
    String destination,
    LocalTime departureTime,
    Set<DayOfWeek> departureDays
) {
}

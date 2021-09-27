package pl.zycienakodach.pragmaticflights.modules.ordering.api.events;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Set;

public record FlightOfferedForSell(
    String flightId,
    String origin,
    String destination,
    LocalTime departureTime,
    Set<DayOfWeek> departureDays
) implements OrderingEvents {
}

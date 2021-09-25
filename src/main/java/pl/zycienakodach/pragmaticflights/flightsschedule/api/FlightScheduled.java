package pl.zycienakodach.pragmaticflights.flightsschedule.api;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Set;

public record FlightScheduled(String flightId, String origin, String destination, LocalTime departureTime, Set<DayOfWeek> departureDays) implements FlightScheduleDomainEvent {
}

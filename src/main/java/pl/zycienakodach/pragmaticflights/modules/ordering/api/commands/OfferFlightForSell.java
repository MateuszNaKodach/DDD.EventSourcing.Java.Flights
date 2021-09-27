package pl.zycienakodach.pragmaticflights.modules.ordering.api.commands;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Set;

public record OfferFlightForSell(
    String flightId,
    String origin,
    String destination,
    LocalTime departureTime,
    Set<DayOfWeek> departureDays
) {
}

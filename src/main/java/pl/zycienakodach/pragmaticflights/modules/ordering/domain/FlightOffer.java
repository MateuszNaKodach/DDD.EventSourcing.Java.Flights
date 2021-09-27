package pl.zycienakodach.pragmaticflights.modules.ordering.domain;

import pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.flightid.FlightId;
import pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.iata.IATAAirportCode;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Set;

public record FlightOffer(
    FlightId flightId,
    IATAAirportCode origin,
    IATAAirportCode destination,
    LocalTime departureTime,
    Set<DayOfWeek> departureDays
) {
}

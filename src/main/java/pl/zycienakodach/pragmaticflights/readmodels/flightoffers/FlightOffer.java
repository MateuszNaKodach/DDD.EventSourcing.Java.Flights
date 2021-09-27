package pl.zycienakodach.pragmaticflights.readmodels.flightoffers;

import pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.flightid.FlightId;
import pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.iata.IATAAirportCode;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Set;

// todo: in readmodel change to primitives
public record FlightOffer(
    FlightId flightId,
    IATAAirportCode origin,
    IATAAirportCode destination,
    LocalTime departureTime,
    Set<DayOfWeek> departureDays
) {
}

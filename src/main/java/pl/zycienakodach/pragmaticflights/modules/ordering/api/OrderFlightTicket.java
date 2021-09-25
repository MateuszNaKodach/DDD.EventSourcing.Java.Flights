package pl.zycienakodach.pragmaticflights.modules.ordering.api;

import java.time.LocalDate;

// Nie mozna kupic na lot, ktory odlecial
// co z discount?
public record OrderFlightTicket(
    String buyerId,
    String flightId,
    LocalDate flightDate
) {
}

//OrderFlightTicket -> FlightTickerOrdered -> CalculatePrice -> PriceCalculated -> SubmitOrder -> OrderSubmittedWithPrice

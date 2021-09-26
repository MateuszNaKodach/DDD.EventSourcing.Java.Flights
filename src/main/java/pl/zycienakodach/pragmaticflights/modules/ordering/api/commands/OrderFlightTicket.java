package pl.zycienakodach.pragmaticflights.modules.ordering.api.commands;

import java.time.LocalDate;

// Nie mozna kupic na lot, ktory odlecial
// co z discount?
public record OrderFlightTicket(
    String orderId,
    String buyerId,
    String flightId,
    LocalDate flightDate
) {
}

//OrderFlightTicket -> FlightTickerOrdered -> CalculatePrice -> PriceCalculated -> SubmitOrder -> OrderSubmittedWithPrice

package pl.zycienakodach.pragmaticflights.modules.ordering.api.events;

import java.time.LocalDate;

public record FlightsOrderSubmitted(
    String orderId,
    String customerId,
    String flightId,
    LocalDate flightDate
) implements OrderingEvents {

}

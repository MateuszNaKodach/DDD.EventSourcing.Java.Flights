package pl.zycienakodach.pragmaticflights.modules.ordering.api.events;

import java.time.LocalDate;
import java.time.LocalTime;

public record FlightsOrderSubmitted(
    String orderId,
    String customerId,
    String flightId,
    LocalTime departureTime,
    LocalDate flightDate,
    String origin,
    String destination
) implements OrderingEvents {

}

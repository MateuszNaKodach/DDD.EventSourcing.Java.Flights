package pl.zycienakodach.pragmaticflights.modules.ordering.api.commands;

import java.time.LocalDate;

public record SubmitFlightOrder(
    String orderId,
    String customerId,
    String flightId,
    LocalDate flightDate
) {
}

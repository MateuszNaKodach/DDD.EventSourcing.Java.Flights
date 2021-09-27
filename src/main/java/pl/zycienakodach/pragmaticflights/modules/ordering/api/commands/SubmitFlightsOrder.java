package pl.zycienakodach.pragmaticflights.modules.ordering.api.commands;

import java.time.LocalDate;

public record SubmitFlightsOrder(
    String orderId,
    String customerId,
    String flightId,
    LocalDate flightDate
) {
}

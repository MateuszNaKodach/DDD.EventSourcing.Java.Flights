package pl.zycienakodach.pragmaticflights.modules.pricing.api.commands;

import java.time.LocalDate;

public record CalculateOrderTotalPrice(
    String orderId,
    String customerId,
    String flightId,
    LocalDate flightDate
) {

}

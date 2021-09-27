package pl.zycienakodach.pragmaticflights.modules.pricing.api.commands;

import java.time.LocalDate;

public record CalculateOrderTotalValue(
    String orderId,
    String buyerId,
    String flightId,
    LocalDate flightDate
) {

}

package pl.zycienakodach.pragmaticflights.modules.pricing.api.commands;

import java.time.LocalDate;
import java.util.Set;

public record CalculateOrderTotalValue(
    String orderId,
    String buyerId,
    Set<Flight> flights
) {

  public static record Flight(String flightId, LocalDate flightDate) {
  }

}

package pl.zycienakodach.pragmaticflights.modules.ordering.api.commands;

import java.time.LocalDate;
import java.util.Set;

public record SubmitFlightsOrder(
    String orderId,
    String customerId,
    Set<Flight> flights
) {
  public static record Flight(String flightId, LocalDate flightDate) {
  }
}

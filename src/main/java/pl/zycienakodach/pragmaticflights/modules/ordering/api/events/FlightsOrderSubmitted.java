package pl.zycienakodach.pragmaticflights.modules.ordering.api.events;

import java.time.LocalDate;
import java.util.Set;

public record FlightsOrderSubmitted(
    String orderId,
    String customerId,
    Set<Flight> flights
) implements OrderingEvents {
  public static record Flight(String flightId, LocalDate flightDate) {
  }
}

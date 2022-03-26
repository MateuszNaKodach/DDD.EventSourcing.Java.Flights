package pl.zycienakodach.esflights.modules.discounts.infrastructure.flightorders;

import java.time.LocalDate;
import java.time.LocalTime;

public record FlightOrderEntity(
    String orderId,
    String customerId,
    LocalDate flightDate,
    Flight flight
) {

  public record Flight(
      String flightId,
      String origin,
      String destination,
      LocalTime departureTime
  ) {
  }
}


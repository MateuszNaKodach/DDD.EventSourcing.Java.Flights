package pl.zycienakodach.pragmaticflights.readmodels.flightorders;

import java.time.LocalDate;
import java.time.LocalTime;

public record FlightOrder(
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


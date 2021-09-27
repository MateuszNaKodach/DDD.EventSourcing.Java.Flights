package pl.zycienakodach.pragmaticflights.modules.discounts.domain.criterias;

import pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.customerid.CustomerId;
import pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.flightid.FlightId;
import pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.iata.IATAAirportCode;
import pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.orderid.OrderId;

import java.time.LocalDate;
import java.time.LocalTime;

public record FlightOrder(
    OrderId orderId,
    CustomerId customerId,
    LocalDate flightDate,
    Flight flight
) {

  public record Flight(
      FlightId flightId,
      IATAAirportCode origin,
      IATAAirportCode destination,
      LocalTime departureTime
  ) {
  }
}

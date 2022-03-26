package pl.zycienakodach.esflights.modules.discounts.domain.criterias;

import pl.zycienakodach.esflights.modules.sharedkernel.domain.customerid.CustomerId;
import pl.zycienakodach.esflights.modules.sharedkernel.domain.flightid.FlightId;
import pl.zycienakodach.esflights.modules.sharedkernel.domain.iata.IATAAirportCode;
import pl.zycienakodach.esflights.modules.sharedkernel.domain.orderid.OrderId;

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

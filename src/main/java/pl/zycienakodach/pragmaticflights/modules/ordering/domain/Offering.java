package pl.zycienakodach.pragmaticflights.modules.ordering.domain;

import pl.zycienakodach.pragmaticflights.modules.ordering.api.events.FlightOfferedForSell;
import pl.zycienakodach.pragmaticflights.modules.ordering.api.events.FlightsOrderSubmitted;
import pl.zycienakodach.pragmaticflights.modules.ordering.api.events.OrderingEvents;
import pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.customerid.CustomerId;
import pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.flightid.FlightId;
import pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.iata.IATAAirportCode;
import pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.orderid.OrderId;
import pl.zycienakodach.pragmaticflights.sdk.domain.DomainLogic;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;

public class Offering {

  public static DomainLogic<OrderingEvents> offerForSell(
      FlightId flightId,
      IATAAirportCode origin,
      IATAAirportCode destination,
      LocalTime departureTime,
      Set<DayOfWeek> departureDays
  ) {
    return (List<OrderingEvents> pastEvents) -> {
      if (departureDays.isEmpty()) {
        throw new IllegalArgumentException("Flight must have at least one departure day.");
      }
      var flightOfferedForSell = new FlightOfferedForSell(
          flightId.raw(),
          origin.raw(),
          destination.raw(),
          departureTime,
          departureDays
      );
      return List.of(flightOfferedForSell);
    };
  }
}

package pl.zycienakodach.pragmaticflights.modules.ordering.domain;

import pl.zycienakodach.pragmaticflights.modules.ordering.api.events.FlightsOrderSubmitted;
import pl.zycienakodach.pragmaticflights.modules.ordering.api.events.OrderingEvents;
import pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.customerid.CustomerId;
import pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.orderid.OrderId;
import pl.zycienakodach.pragmaticflights.readmodels.flightoffers.FlightOffer;
import pl.zycienakodach.pragmaticflights.sdk.domain.DomainLogic;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.function.BiConsumer;

public class Ordering {

  public static DomainLogic<OrderingEvents> submitFlightOrder(
      OrderId orderId,
      CustomerId customerId,
      FlightOffer offer,
      LocalDate departureDate,
      Instant currentTime
  ) {
    return (List<OrderingEvents> pastEvents) -> {
      var state = rehydrate(pastEvents);
      if (state.alreadyOrdered) {
        throw new RuntimeException("Order already submitted");
      }
      var departureOnSelectedDay = offer.departureDays().stream().anyMatch(d -> d.equals(departureDate.getDayOfWeek()));
      if (!departureOnSelectedDay) {
        throw new RuntimeException("Cannot order flight ticket for day when the flight does not departures!");
      }
      var departureDateTime = LocalDateTime.of(departureDate, offer.departureTime());
      if (departureDateTime.toInstant(ZoneOffset.UTC).isAfter(currentTime)) {
        throw new IllegalStateException("The flight has already departure!");
      }
      return List.of(
          new FlightsOrderSubmitted(
              orderId.raw(),
              customerId.raw(),
              offer.flightId().raw(),
              offer.departureTime(),
              departureDate,
              offer.origin().raw(),
              offer.destination().raw()
          )
      );
    };
  }

  private static OrderState rehydrate(List<OrderingEvents> events) {
    return events.stream().collect(OrderState::new, (state, event) -> {
      if (event instanceof FlightsOrderSubmitted) {
        state.alreadyOrdered = true;
      }
    }, EMPTY);
  }

  private static class OrderState {
    private boolean alreadyOrdered;
  }

  private static final BiConsumer<OrderState, OrderState> EMPTY = (gameState, gameState2) -> {
  };
}

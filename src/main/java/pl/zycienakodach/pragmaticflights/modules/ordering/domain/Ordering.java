package pl.zycienakodach.pragmaticflights.modules.ordering.domain;

import pl.zycienakodach.pragmaticflights.modules.ordering.api.events.FlightCourseOfferedForSell;
import pl.zycienakodach.pragmaticflights.modules.ordering.api.events.FlightOrderSubmitted;
import pl.zycienakodach.pragmaticflights.modules.ordering.api.events.OrderingEvents;
import pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.customerid.CustomerId;
import pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.flightid.FlightCourseId;
import pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.iata.IATAAirportCode;
import pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.orderid.OrderId;
import pl.zycienakodach.pragmaticflights.sdk.domain.DomainLogic;

import java.time.Instant;
import java.util.List;
import java.util.function.BiConsumer;

public class Ordering {

  public static DomainLogic<OrderingEvents> submitFlightCourseOrder(
      OrderId orderId,
      CustomerId customerId,
      FlightCourseId flightCourseId,
      Instant currentTime
  ) {
    return (List<OrderingEvents> pastEvents) -> {
      var state = rehydrate(customerId, pastEvents);
      if (state.alreadyOrdered) {
        throw new RuntimeException("Order already submitted");
      }
      var departureDateTime = flightCourseId.departureAt();
      if (currentTime.isAfter(departureDateTime)) {
        throw new IllegalStateException("The flight has already departure!");
      }
      return List.of(
          new FlightOrderSubmitted(
              orderId.raw(),
              customerId.raw(),
              flightCourseId.raw(),
              state.origin.raw(),
              state.destination.raw()
          )
      );
    };
  }

  private static OrderState rehydrate(CustomerId customerId, List<OrderingEvents> events) {
    return events.stream()
        .collect(OrderState::new, (state, event) -> {
          if (event instanceof FlightCourseOfferedForSell flightOfferedForSell) {
            state.origin = IATAAirportCode.fromRaw(flightOfferedForSell.origin());
            state.destination = IATAAirportCode.fromRaw(flightOfferedForSell.destination());
          }
          if (event instanceof FlightOrderSubmitted flightOrderSubmitted) {
            if (flightOrderSubmitted.customerId().equals(customerId.raw())) {
              state.alreadyOrdered = true;
            }
          }
        }, EMPTY);
  }

  private static class OrderState {
    private IATAAirportCode origin;
    private IATAAirportCode destination;
    private boolean alreadyOrdered;
  }

  private static final BiConsumer<OrderState, OrderState> EMPTY = (gameState, gameState2) -> {
  };
}

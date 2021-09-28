package pl.zycienakodach.pragmaticflights.modules.pricing.domain;

import pl.zycienakodach.pragmaticflights.modules.ordering.api.events.FlightsOrderSubmitted;
import pl.zycienakodach.pragmaticflights.modules.ordering.api.events.OrderingEvents;
import pl.zycienakodach.pragmaticflights.modules.ordering.domain.Ordering;
import pl.zycienakodach.pragmaticflights.modules.pricing.api.events.CalculateOrderTotalPriceCompleted;
import pl.zycienakodach.pragmaticflights.modules.pricing.api.events.CalculateOrderTotalPriceStarted;
import pl.zycienakodach.pragmaticflights.modules.pricing.api.events.PricingEvent;
import pl.zycienakodach.pragmaticflights.modules.pricing.api.events.RegularPriceDefined;
import pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.flightid.FlightId;
import pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.money.EuroMoney;
import pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.orderid.OrderId;
import pl.zycienakodach.pragmaticflights.sdk.domain.DomainLogic;

import java.time.DayOfWeek;
import java.util.List;
import java.util.function.BiConsumer;

public class Pricing {

  public static DomainLogic<PricingEvent> defineRegularPrice(
      FlightId flightId,
      DayOfWeek dayOfWeek,
      EuroMoney regularPrice
  ) {
    return (List<PricingEvent> pastEvents) -> List.of(
        new RegularPriceDefined(
            flightId.raw(),
            dayOfWeek,
            regularPrice.value()
        )
    );
  }

  public static DomainLogic<PricingEvent> startCalculatingOrderTotalPrice(
      OrderId orderId,
      EuroMoney regularPrice
  ) {
    return (List<PricingEvent> pastEvents) -> {
      if (regularPrice == null) {
        throw new IllegalStateException("Cannot calculate order total for not known regular price!");
      }
      return List.of(
          new CalculateOrderTotalPriceStarted(
              orderId.raw(),
              regularPrice.value()
          )
      );
    };
  }

  public static DomainLogic<PricingEvent> completeCalculatingOrderTotalPrice(
      OrderId orderId,
      EuroMoney discountPrice
  ) {
    return (List<PricingEvent> pastEvents) -> {
      var price = rehydrate(pastEvents);
      return List.of(
          new CalculateOrderTotalPriceCompleted(
              orderId.raw(),
              price.regular.value(),
              discountPrice.value(),
              price.regular.minus(discountPrice).value()
          )
      );
    };
  }

  private static OrderPrice rehydrate(List<PricingEvent> events) {
    return events.stream().collect(OrderPrice::new, (state, event) -> {
      if (event instanceof CalculateOrderTotalPriceStarted) {
        state.regular = EuroMoney.of(((CalculateOrderTotalPriceStarted) event).regularPriceInEuro());
      }
      if (event instanceof CalculateOrderTotalPriceCompleted) {
        state.discount = EuroMoney.of(((CalculateOrderTotalPriceCompleted) event).discountPriceInEuro());
        state.total = EuroMoney.of(((CalculateOrderTotalPriceCompleted) event).totalPriceInEuro());
      }
    }, EMPTY);
  }

  private static class OrderPrice {
    private EuroMoney regular;
    private EuroMoney discount;
    private EuroMoney total;
  }

  private static final BiConsumer<OrderPrice, OrderPrice> EMPTY = (gameState, gameState2) -> {
  };

}

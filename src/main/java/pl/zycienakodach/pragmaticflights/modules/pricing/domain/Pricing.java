package pl.zycienakodach.pragmaticflights.modules.pricing.domain;

import pl.zycienakodach.pragmaticflights.modules.pricing.api.events.CalculateOrderTotalPriceCompleted;
import pl.zycienakodach.pragmaticflights.modules.pricing.api.events.CalculateOrderTotalPriceStarted;
import pl.zycienakodach.pragmaticflights.modules.pricing.api.events.PricingEvent;
import pl.zycienakodach.pragmaticflights.modules.pricing.api.events.RegularPriceDefined;
import pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.flightid.FlightCourseId;
import pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.money.EuroMoney;
import pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.orderid.OrderId;
import pl.zycienakodach.pragmaticflights.sdk.domain.DomainLogic;

import java.util.List;
import java.util.function.BiConsumer;

public class Pricing {

  public static DomainLogic<PricingEvent> defineRegularPrice(
      FlightCourseId flightCourseId,
      EuroMoney regularPrice
  ) {
    return (List<PricingEvent> pastEvents) -> List.of(
        new RegularPriceDefined(
            flightCourseId.raw(),
            regularPrice.value()
        )
    );
  }

  public static DomainLogic<PricingEvent> startCalculatingOrderTotalPrice(
      OrderId orderId
  ) {
    return (List<PricingEvent> pastEvents) -> {
      var price = rehydrate(orderId, pastEvents);
      return List.of(
          new CalculateOrderTotalPriceStarted(
              orderId.raw(),
              price.regular.value()
          )
      );
    };
  }

  public static DomainLogic<PricingEvent> completeCalculatingOrderTotalPrice(
      OrderId orderId,
      EuroMoney discountPrice
  ) {
    return (List<PricingEvent> pastEvents) -> {
      var price = rehydrate(orderId, pastEvents);
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

  private static OrderPrice rehydrate(OrderId orderId, List<PricingEvent> events) {
    return events.stream()
        .collect(OrderPrice::new, (state, event) -> {
          if (event instanceof final RegularPriceDefined regularPriceDefined) {
            state.regular = EuroMoney.of(regularPriceDefined.priceInEuro());
          }
          if (event instanceof final CalculateOrderTotalPriceCompleted calculateOrderTotalPriceCompleted) {
            if (calculateOrderTotalPriceCompleted.orderId().equals(orderId.raw())) {
              state.discount = EuroMoney.of(calculateOrderTotalPriceCompleted.discountPriceInEuro());
              state.total = EuroMoney.of(calculateOrderTotalPriceCompleted.totalPriceInEuro());
            }
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

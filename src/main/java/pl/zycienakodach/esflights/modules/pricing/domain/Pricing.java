package pl.zycienakodach.esflights.modules.pricing.domain;

import pl.zycienakodach.esflights.modules.pricing.api.events.CalculateOrderTotalPriceCompleted;
import pl.zycienakodach.esflights.modules.pricing.api.events.CalculateOrderTotalPriceStarted;
import pl.zycienakodach.esflights.modules.pricing.api.events.DiscountApplied;
import pl.zycienakodach.esflights.modules.pricing.api.events.PricingEvent;
import pl.zycienakodach.esflights.modules.pricing.api.events.RegularPriceDefined;
import pl.zycienakodach.esflights.modules.sharedkernel.domain.flightid.FlightCourseId;
import pl.zycienakodach.esflights.modules.sharedkernel.domain.money.EuroMoney;
import pl.zycienakodach.esflights.modules.sharedkernel.domain.orderid.OrderId;
import pl.zycienakodach.esflights.sdk.domain.DomainLogic;

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
            regularPrice.toBigDecimal()
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
              price.regular.toBigDecimal()
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
          new DiscountApplied(orderId.raw(), discountPrice.toBigDecimal()),
          new CalculateOrderTotalPriceCompleted(
              orderId.raw(),
              price.regular.toBigDecimal(),
              discountPrice.toBigDecimal(),
              price.regular.subtract(discountPrice).toBigDecimal()
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

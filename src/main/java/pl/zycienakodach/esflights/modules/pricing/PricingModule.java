package pl.zycienakodach.esflights.modules.pricing;

import pl.zycienakodach.esflights.modules.pricing.api.commands.ApplyOrderPriceDiscount;
import pl.zycienakodach.esflights.modules.pricing.api.commands.CalculateOrderTotalPrice;
import pl.zycienakodach.esflights.modules.pricing.api.commands.DefineRegularPrice;
import pl.zycienakodach.esflights.modules.sharedkernel.domain.flightid.FlightCourseId;
import pl.zycienakodach.esflights.modules.sharedkernel.domain.money.EuroMoney;
import pl.zycienakodach.esflights.modules.sharedkernel.domain.orderid.OrderId;
import pl.zycienakodach.esflights.sdk.Application;
import pl.zycienakodach.esflights.sdk.ApplicationModule;
import pl.zycienakodach.esflights.sdk.application.eventstream.EventStreamName;

import static pl.zycienakodach.esflights.modules.pricing.domain.Pricing.completeCalculatingOrderTotalPrice;
import static pl.zycienakodach.esflights.modules.pricing.domain.Pricing.defineRegularPrice;
import static pl.zycienakodach.esflights.modules.pricing.domain.Pricing.startCalculatingOrderTotalPrice;

public class PricingModule implements ApplicationModule {

  public PricingModule configure(Application app) {
    app
        .onCommand(
            DefineRegularPrice.class,
            (c, m) -> eventStreamNameFor(c.flightCourseId()),
            (c) -> defineRegularPrice(
                FlightCourseId.fromRaw(c.flightCourseId()),
                EuroMoney.of(c.priceInEuro())
            )
        ).onCommand(
            CalculateOrderTotalPrice.class,
            (c, m) -> eventStreamNameFor(c),
            (c) -> startCalculatingOrderTotalPrice(OrderId.fromRaw(c.orderId()))
        ).onCommand(
            ApplyOrderPriceDiscount.class,
            (c, m) -> eventStreamNameFor(c),
            (c) -> completeCalculatingOrderTotalPrice(
                OrderId.fromRaw(c.orderId()),
                EuroMoney.of(c.discountInEuro())
            )
        );
    return this;
  }

  private EventStreamName eventStreamNameFor(CalculateOrderTotalPrice c) {
    final OrderId orderId = OrderId.fromRaw(c.orderId());
    return eventStreamNameFor(orderId.flightCourseId().raw());
  }

  private EventStreamName eventStreamNameFor(ApplyOrderPriceDiscount c) {
    final OrderId orderId = OrderId.fromRaw(c.orderId());
    return eventStreamNameFor(orderId.flightCourseId().raw());
  }

  private EventStreamName eventStreamNameFor(String rawFlightCourseId) {
    return EventStreamName.ofCategory("FlightCourseOrdersPricing").withId(rawFlightCourseId);
  }

}

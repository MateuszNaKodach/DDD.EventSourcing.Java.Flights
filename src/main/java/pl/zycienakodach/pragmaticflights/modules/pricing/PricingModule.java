package pl.zycienakodach.pragmaticflights.modules.pricing;

import pl.zycienakodach.pragmaticflights.modules.pricing.api.commands.ApplyOrderPriceDiscount;
import pl.zycienakodach.pragmaticflights.modules.pricing.api.commands.CalculateOrderTotalPrice;
import pl.zycienakodach.pragmaticflights.modules.pricing.api.commands.DefineRegularPrice;
import pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.flightid.FlightCourseId;
import pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.money.EuroMoney;
import pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.orderid.OrderId;
import pl.zycienakodach.pragmaticflights.sdk.Application;
import pl.zycienakodach.pragmaticflights.sdk.ApplicationModule;
import pl.zycienakodach.pragmaticflights.sdk.application.eventstream.EventStreamName;

import static pl.zycienakodach.pragmaticflights.modules.pricing.domain.Pricing.completeCalculatingOrderTotalPrice;
import static pl.zycienakodach.pragmaticflights.modules.pricing.domain.Pricing.defineRegularPrice;
import static pl.zycienakodach.pragmaticflights.modules.pricing.domain.Pricing.startCalculatingOrderTotalPrice;
import static pl.zycienakodach.pragmaticflights.sdk.application.eventstream.EventStreamName.category;
import static pl.zycienakodach.pragmaticflights.sdk.application.eventstream.EventStreamName.streamId;

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
    return new EventStreamName(category("FlightCourseOrdersPricing"), streamId(rawFlightCourseId));
  }

}

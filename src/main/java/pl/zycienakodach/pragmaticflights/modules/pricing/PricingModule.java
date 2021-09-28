package pl.zycienakodach.pragmaticflights.modules.pricing;

import pl.zycienakodach.pragmaticflights.modules.pricing.api.commands.ApplyDiscount;
import pl.zycienakodach.pragmaticflights.modules.pricing.api.commands.CalculateOrderTotalPrice;
import pl.zycienakodach.pragmaticflights.modules.pricing.api.commands.DefineRegularPrice;
import pl.zycienakodach.pragmaticflights.modules.pricing.application.RegularPrices;
import pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.flightid.FlightId;
import pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.money.EuroMoney;
import pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.orderid.OrderId;
import pl.zycienakodach.pragmaticflights.sdk.Application;
import pl.zycienakodach.pragmaticflights.sdk.ApplicationModule;
import pl.zycienakodach.pragmaticflights.sdk.application.EventStreamName;

import static pl.zycienakodach.pragmaticflights.modules.pricing.domain.Pricing.completeCalculatingOrderTotalPrice;
import static pl.zycienakodach.pragmaticflights.modules.pricing.domain.Pricing.defineRegularPrice;
import static pl.zycienakodach.pragmaticflights.modules.pricing.domain.Pricing.startCalculatingOrderTotalPrice;
import static pl.zycienakodach.pragmaticflights.sdk.application.EventStreamName.category;
import static pl.zycienakodach.pragmaticflights.sdk.application.EventStreamName.streamId;

public class PricingModule implements ApplicationModule {

  private final RegularPrices regularPrices;

  public PricingModule(RegularPrices regularPrices) {
    this.regularPrices = regularPrices;
  }

  public PricingModule configure(Application app) {
    app.onCommand(
        DefineRegularPrice.class,
        (c, m) -> new EventStreamName(category(m.tenantId().raw(), "FlightPrice"), streamId(c.flightId(), c.dayOfWeek().name())),
        (c) -> defineRegularPrice(
            FlightId.fromRaw(c.flightId()),
            c.dayOfWeek(),
            EuroMoney.of(c.priceInEuro())
        )
    );
    app.onCommand(
        CalculateOrderTotalPrice.class,
        (c, m) -> new EventStreamName(category(m.tenantId().raw(), "OrderTotalPrice"), streamId(c.orderId())),
        (c) -> {
          var flightId = FlightId.fromRaw(c.flightId());
          var price = this.regularPrices.findBy(flightId, c.flightDate().getDayOfWeek());
          return startCalculatingOrderTotalPrice(
              new OrderId(c.orderId()),
              price.orElse(null)
          );
        }
    );
    app.onCommand(
        ApplyDiscount.class,
        (c, m) -> new EventStreamName(category(m.tenantId().raw(), "OrderTotalPrice"), streamId(c.orderId())),
        (c) -> completeCalculatingOrderTotalPrice(
            new OrderId(c.orderId()),
            EuroMoney.of(c.discountInEuro())
        )
    );
    return this;
  }

}

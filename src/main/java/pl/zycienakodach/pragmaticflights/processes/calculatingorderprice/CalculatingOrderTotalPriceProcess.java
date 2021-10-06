package pl.zycienakodach.pragmaticflights.processes.calculatingorderprice;

import pl.zycienakodach.pragmaticflights.modules.discounts.api.event.DiscountValueCalculated;
import pl.zycienakodach.pragmaticflights.modules.ordering.api.events.FlightOrderSubmitted;
import pl.zycienakodach.pragmaticflights.modules.pricing.api.commands.ApplyOrderPriceDiscount;
import pl.zycienakodach.pragmaticflights.modules.pricing.api.commands.CalculateOrderTotalPrice;
import pl.zycienakodach.pragmaticflights.sdk.Application;
import pl.zycienakodach.pragmaticflights.sdk.ApplicationModule;

public class CalculatingOrderTotalPriceProcess implements ApplicationModule {

  @Override
  public ApplicationModule configure(Application app) {
    app
        .when(FlightOrderSubmitted.class, (e) ->
            new CalculateOrderTotalPrice(
                e.orderId()
            )
        ).when(DiscountValueCalculated.class, (e) ->
            new ApplyOrderPriceDiscount(
                e.orderId(),
                e.discountInEuro()
            )
        );
    return this;
  }

}

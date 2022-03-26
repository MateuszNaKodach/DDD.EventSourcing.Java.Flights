package pl.zycienakodach.esflights.processes.calculatingorderprice;

import pl.zycienakodach.esflights.modules.discounts.api.event.DiscountValueCalculated;
import pl.zycienakodach.esflights.modules.ordering.api.events.FlightOrderSubmitted;
import pl.zycienakodach.esflights.modules.pricing.api.commands.ApplyOrderPriceDiscount;
import pl.zycienakodach.esflights.modules.pricing.api.commands.CalculateOrderTotalPrice;
import pl.zycienakodach.esflights.sdk.Application;
import pl.zycienakodach.esflights.sdk.ApplicationModule;

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

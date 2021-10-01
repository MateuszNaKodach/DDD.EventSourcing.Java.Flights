package pl.zycienakodach.pragmaticflights.processes.calculatingorderprice;

import pl.zycienakodach.pragmaticflights.modules.discounts.api.event.DiscountValueCalculated;
import pl.zycienakodach.pragmaticflights.modules.ordering.api.events.FlightsOrderSubmitted;
import pl.zycienakodach.pragmaticflights.modules.pricing.api.commands.ApplyOrderPriceDiscount;
import pl.zycienakodach.pragmaticflights.modules.pricing.api.commands.CalculateOrderTotalPrice;
import pl.zycienakodach.pragmaticflights.sdk.Application;
import pl.zycienakodach.pragmaticflights.sdk.ApplicationModule;
import pl.zycienakodach.pragmaticflights.sdk.application.message.CausationId;
import pl.zycienakodach.pragmaticflights.sdk.application.message.command.CommandId;
import pl.zycienakodach.pragmaticflights.sdk.application.message.command.CommandMetadata;

public class CalculatingOrderTotalPriceProcess implements ApplicationModule {

  @Override
  public ApplicationModule configure(Application app) {
    app
        .when(FlightsOrderSubmitted.class, (e) ->
            new CalculateOrderTotalPrice(
                e.orderId(),
                e.flightId(),
                e.flightDate()
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

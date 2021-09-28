package pl.zycienakodach.pragmaticflights.processes.calculatingorderprice;

import pl.zycienakodach.pragmaticflights.modules.discounts.api.event.DiscountValueCalculated;
import pl.zycienakodach.pragmaticflights.modules.ordering.api.events.FlightsOrderSubmitted;
import pl.zycienakodach.pragmaticflights.modules.pricing.api.commands.ApplyDiscount;
import pl.zycienakodach.pragmaticflights.modules.pricing.api.commands.CalculateOrderTotalPrice;
import pl.zycienakodach.pragmaticflights.sdk.Application;
import pl.zycienakodach.pragmaticflights.sdk.ApplicationModule;
import pl.zycienakodach.pragmaticflights.sdk.application.message.CausationId;
import pl.zycienakodach.pragmaticflights.sdk.application.message.command.CommandId;
import pl.zycienakodach.pragmaticflights.sdk.application.message.command.CommandMetadata;

public class CalculatingOrderTotalPriceProcess implements ApplicationModule {

  @Override
  public ApplicationModule configure(Application app) {
    app.when(FlightsOrderSubmitted.class, (e, m) ->
        app.execute(
            new CalculateOrderTotalPrice(
                e.orderId(),
                e.flightId(),
                e.flightDate()
            ),
            new CommandMetadata(
                new CommandId(app.generateId()),
                m.tenantId(),
                m.correlationId(),
                new CausationId(m.eventId().raw())
            )
        ));
    app.when(DiscountValueCalculated.class, (e, m) -> app.execute(
        new ApplyDiscount(
            e.orderId(),
            e.discountInEuro()
        ),
        new CommandMetadata(
            new CommandId(app.generateId()),
            m.tenantId(),
            m.correlationId(),
            new CausationId(m.eventId().raw())
        )
    ));
    return this;
  }

}

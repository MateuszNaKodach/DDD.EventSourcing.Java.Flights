package pl.zycienakodach.pragmaticflights.processes.calculatingorderprice;

import pl.zycienakodach.pragmaticflights.modules.ordering.api.events.FlightsOrderSubmitted;
import pl.zycienakodach.pragmaticflights.modules.pricing.api.commands.CalculateOrderTotalValue;
import pl.zycienakodach.pragmaticflights.sdk.Application;
import pl.zycienakodach.pragmaticflights.sdk.ApplicationModule;
import pl.zycienakodach.pragmaticflights.sdk.application.message.CausationId;
import pl.zycienakodach.pragmaticflights.sdk.application.message.command.CommandId;
import pl.zycienakodach.pragmaticflights.sdk.application.message.command.CommandMetadata;

class CalculatingOrderPrice implements ApplicationModule {

  @Override
  public ApplicationModule configure(Application app) {
    app.when(FlightsOrderSubmitted.class, (e, m) ->
        app.execute(
            new CalculateOrderTotalValue(
                e.orderId(),
                e.customerId(),
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
    return this;
  }

}

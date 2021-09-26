package pl.zycienakodach.pragmaticflights.modules.pricing;

import pl.zycienakodach.pragmaticflights.modules.pricing.api.commands.DefineRegularPrice;
import pl.zycienakodach.pragmaticflights.sdk.Application;
import pl.zycienakodach.pragmaticflights.sdk.ApplicationModule;
import pl.zycienakodach.pragmaticflights.sdk.application.EventStreamName;

import java.util.List;

import static pl.zycienakodach.pragmaticflights.sdk.application.EventStreamName.category;
import static pl.zycienakodach.pragmaticflights.sdk.application.EventStreamName.streamId;

public class PricingModule implements ApplicationModule {

  public PricingModule configure(Application app){
    app.onCommand(
        DefineRegularPrice.class,
        (c,m) -> new EventStreamName(category(m.tenantId().raw(), "FlightPrice"), streamId(c.flightId(), c.dayOfWeek().name())),
        (c) -> (__) -> List.of()
    );
    return this;
  }

}

package pl.zycienakodach.pragmaticflights.pricing;

import pl.zycienakodach.pragmaticflights.pricing.api.DefineFlightRegularPrice;
import pl.zycienakodach.pragmaticflights.shared.Application;
import pl.zycienakodach.pragmaticflights.shared.ApplicationModule;
import pl.zycienakodach.pragmaticflights.shared.application.EventStreamName;

import java.util.List;

import static pl.zycienakodach.pragmaticflights.shared.application.EventStreamName.category;
import static pl.zycienakodach.pragmaticflights.shared.application.EventStreamName.streamId;

public class PricingModule implements ApplicationModule {

  public PricingModule configure(Application app){
    app.onCommand(
        DefineFlightRegularPrice.class,
        (c,m) -> new EventStreamName(category(m.tenantId().raw(), "FlightPrice"), streamId(c.dayOfWeek().name(), c.flightId())),
        (c) -> (__) -> List.of()
    );
    return this;
  }

}

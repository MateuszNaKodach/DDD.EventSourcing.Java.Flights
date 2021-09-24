package pl.zycienakodach.pragmaticflights.pricing;

import io.vavr.control.Either;
import pl.zycienakodach.pragmaticflights.pricing.api.DefineFlightPrice;
import pl.zycienakodach.pragmaticflights.shared.Application;
import pl.zycienakodach.pragmaticflights.shared.ApplicationModule;
import pl.zycienakodach.pragmaticflights.shared.application.EventStreamName;

import java.util.List;

import static pl.zycienakodach.pragmaticflights.shared.application.EventStreamName.category;
import static pl.zycienakodach.pragmaticflights.shared.application.EventStreamName.streamId;

public class PricingModule implements ApplicationModule {

  public PricingModule configure(Application app){
    app.onCommand(
        DefineFlightPrice.class,
        (c,m) -> new EventStreamName(category(m.tenantId().raw(), "FlightPrice"), streamId(c.dayOfWeek().name(), c.flightId())),
        (__) -> Either.right(List.of())
    );
    return this;
  }

}

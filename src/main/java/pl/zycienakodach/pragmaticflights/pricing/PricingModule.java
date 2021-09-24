package pl.zycienakodach.pragmaticflights.pricing;

import io.vavr.control.Either;
import pl.zycienakodach.pragmaticflights.pricing.api.DefineFlightPrice;
import pl.zycienakodach.pragmaticflights.shared.Application;
import pl.zycienakodach.pragmaticflights.shared.ApplicationModule;
import pl.zycienakodach.pragmaticflights.shared.application.EventStreamName;

import java.util.List;

public class PricingModule implements ApplicationModule {

  //tenant here? / wrapper like tenantFeature? / inject context for commands / eventHandlers - how? / execution context
  public PricingModule() {
  }

  public PricingModule configure(Application app){
    app.onCommand(
        DefineFlightPrice.class,
        c -> new EventStreamName("FlightPrice", c.flightId()),
        (__) -> Either.right(List.of())
    );
    return this;
  }

}

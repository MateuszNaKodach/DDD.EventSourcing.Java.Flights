package pl.zycienakodach.pragmaticflights.pricing;

import io.vavr.control.Either;
import pl.zycienakodach.pragmaticflights.pricing.api.DefineFlightPrice;
import pl.zycienakodach.pragmaticflights.shared.Application;
import pl.zycienakodach.pragmaticflights.shared.application.ApplicationService;
import pl.zycienakodach.pragmaticflights.shared.application.EventStreamName;

import java.util.List;

public class PricingModule {

  //tenant here? / wrapper like tenantFeature? / inject context for commands / eventHandlers - how? / execution context
  private final ApplicationService applicationService;

  public PricingModule(ApplicationService applicationService) {
    this.applicationService = applicationService;
  }

  public PricingModule configure(Application app){
    app.onCommand(
        DefineFlightPrice.class,
        c -> applicationService.execute(
            new EventStreamName("FlightPrice", c.flightId()),
            (__) -> Either.right(List.of())
        )
    );
    return this;
  }

}

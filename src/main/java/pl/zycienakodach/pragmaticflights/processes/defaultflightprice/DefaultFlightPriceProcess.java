package pl.zycienakodach.pragmaticflights.processes.defaultflightprice;

import pl.zycienakodach.pragmaticflights.modules.ordering.api.events.FlightCourseOfferedForSell;
import pl.zycienakodach.pragmaticflights.modules.pricing.api.commands.DefineRegularPrice;
import pl.zycienakodach.pragmaticflights.sdk.Application;
import pl.zycienakodach.pragmaticflights.sdk.ApplicationModule;

/**
 * May be modified to set price by date / day of week etc.
 */
public class DefaultFlightPriceProcess implements ApplicationModule {

  private final double defaultPriceInEuro;

  public DefaultFlightPriceProcess(double defaultPriceInEuro) {
    this.defaultPriceInEuro = defaultPriceInEuro;
  }

  @Override
  public ApplicationModule configure(Application app) {
    app.when(FlightCourseOfferedForSell.class, (e) ->
        new DefineRegularPrice(
            e.flightCourseId(),
            defaultPriceInEuro
        )
    );
    return this;
  }
}

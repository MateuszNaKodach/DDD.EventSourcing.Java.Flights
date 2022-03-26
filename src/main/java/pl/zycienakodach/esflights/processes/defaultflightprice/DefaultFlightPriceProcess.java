package pl.zycienakodach.esflights.processes.defaultflightprice;

import pl.zycienakodach.esflights.modules.ordering.api.events.FlightCourseOfferedForSell;
import pl.zycienakodach.esflights.modules.pricing.api.commands.DefineRegularPrice;
import pl.zycienakodach.esflights.sdk.Application;
import pl.zycienakodach.esflights.sdk.ApplicationModule;

import java.math.BigDecimal;

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
            BigDecimal.valueOf(defaultPriceInEuro)
        )
    );
    return this;
  }
}

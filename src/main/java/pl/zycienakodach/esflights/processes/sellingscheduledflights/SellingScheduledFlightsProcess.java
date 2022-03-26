package pl.zycienakodach.esflights.processes.sellingscheduledflights;

import pl.zycienakodach.esflights.modules.flightsschedule.api.events.FlightCourseScheduled;
import pl.zycienakodach.esflights.modules.ordering.api.commands.OfferFlightCourseForSell;
import pl.zycienakodach.esflights.sdk.Application;
import pl.zycienakodach.esflights.sdk.ApplicationModule;

public class SellingScheduledFlightsProcess implements ApplicationModule {

  @Override
  public ApplicationModule configure(Application app) {
    app.when(FlightCourseScheduled.class, (e) ->
        new OfferFlightCourseForSell(
            e.flightCourseId(),
            e.origin(),
            e.destination()
        ));
    return this;
  }
}

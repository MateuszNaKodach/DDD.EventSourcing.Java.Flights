package pl.zycienakodach.pragmaticflights.processes.sellingscheduledflights;

import pl.zycienakodach.pragmaticflights.modules.flightsschedule.api.events.FlightCourseScheduled;
import pl.zycienakodach.pragmaticflights.modules.ordering.api.commands.OfferFlightCourseForSell;
import pl.zycienakodach.pragmaticflights.sdk.Application;
import pl.zycienakodach.pragmaticflights.sdk.ApplicationModule;

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

package pl.zycienakodach.pragmaticflights.processes.sellingscheduledflights;

import pl.zycienakodach.pragmaticflights.modules.flightsschedule.api.events.FlightScheduled;
import pl.zycienakodach.pragmaticflights.modules.ordering.api.commands.OfferFlightForSell;
import pl.zycienakodach.pragmaticflights.sdk.Application;
import pl.zycienakodach.pragmaticflights.sdk.ApplicationModule;

import java.time.LocalTime;
import java.util.Collections;

public class SellingScheduledFlightsProcess implements ApplicationModule {

  @Override
  public ApplicationModule configure(Application app) {
    app.when(FlightScheduled.class, (e) -> new OfferFlightForSell(
        e.flightId(),
        e.origin(),
        e.destination(),
        LocalTime.now(),
        Collections.emptySet()
    ));
    return this;
  }
}

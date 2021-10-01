package pl.zycienakodach.pragmaticflights.processes.sellingscheduledflights;

import pl.zycienakodach.pragmaticflights.modules.flightsschedule.api.events.FlightScheduled;
import pl.zycienakodach.pragmaticflights.modules.ordering.api.commands.OfferFlightForSell;
import pl.zycienakodach.pragmaticflights.sdk.Application;
import pl.zycienakodach.pragmaticflights.sdk.ApplicationModule;
import pl.zycienakodach.pragmaticflights.sdk.application.message.CausationId;
import pl.zycienakodach.pragmaticflights.sdk.application.message.command.CommandId;
import pl.zycienakodach.pragmaticflights.sdk.application.message.command.CommandMetadata;

public class SellingScheduledFlightsProcess implements ApplicationModule {

  @Override
  public ApplicationModule configure(Application app) {
    app.when(FlightScheduled.class, (e) -> new OfferFlightForSell(
        e.flightId(),
        e.origin(),
        e.destination(),
        e.departureTime(),
        e.departureDays()
    ));
    return this;
  }
}

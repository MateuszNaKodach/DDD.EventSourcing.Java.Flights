package pl.zycienakodach.pragmaticflights.processes.defaultflightprice;

import pl.zycienakodach.pragmaticflights.modules.flightsschedule.api.events.FlightScheduled;
import pl.zycienakodach.pragmaticflights.modules.pricing.api.commands.DefineRegularPrice;
import pl.zycienakodach.pragmaticflights.sdk.Application;
import pl.zycienakodach.pragmaticflights.sdk.ApplicationModule;
import pl.zycienakodach.pragmaticflights.sdk.application.message.CausationId;
import pl.zycienakodach.pragmaticflights.sdk.application.message.command.CommandId;
import pl.zycienakodach.pragmaticflights.sdk.application.message.command.CommandMetadata;

class DefaultFlightPrice implements ApplicationModule {

  private final double defaultPriceInEuro;

  DefaultFlightPrice(double defaultPriceInEuro) {
    this.defaultPriceInEuro = defaultPriceInEuro;
  }

  @Override
  public ApplicationModule configure(Application app) {
    app.when(FlightScheduled.class, (e, m) -> {
      e.departureDays().forEach(day -> {
        app.execute(
            new DefineRegularPrice(
                e.flightId(),
                day,
                defaultPriceInEuro
            ),
            new CommandMetadata(
                new CommandId(app.generateId()),
                m.tenantId(),
                m.correlationId(),
                new CausationId(m.eventId().raw())
            )
        );
      });
    });
    return this;
  }
}

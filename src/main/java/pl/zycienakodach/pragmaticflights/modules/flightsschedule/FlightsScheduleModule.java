package pl.zycienakodach.pragmaticflights.modules.flightsschedule;

import pl.zycienakodach.pragmaticflights.modules.flightsschedule.api.ScheduleFlight;
import pl.zycienakodach.pragmaticflights.sdk.Application;
import pl.zycienakodach.pragmaticflights.sdk.ApplicationModule;
import pl.zycienakodach.pragmaticflights.sdk.application.EventStreamName;
import pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.flightid.FlightIdFactory;
import pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.iata.IATAAirportCodeFactory;

import static pl.zycienakodach.pragmaticflights.modules.flightsschedule.domain.FlightScheduling.scheduleFlight;
import static pl.zycienakodach.pragmaticflights.sdk.application.EventStreamName.category;
import static pl.zycienakodach.pragmaticflights.sdk.application.EventStreamName.streamId;

public class FlightsScheduleModule implements ApplicationModule {

  private final FlightIdFactory flightIdFactory;
  private final IATAAirportCodeFactory iataAirportCodeFactory;

  FlightsScheduleModule(FlightIdFactory flightIdFactory, IATAAirportCodeFactory iataAirportCodeFactory) {
    this.flightIdFactory = flightIdFactory;
    this.iataAirportCodeFactory = iataAirportCodeFactory;
  }
  @Override
  public ApplicationModule configure(Application app) {
    app.onCommand(
        ScheduleFlight.class,
        (c,m) -> new EventStreamName(category(m.tenantId().raw(), "FlightSchedule"), streamId(c.flightId())),
        (c) -> scheduleFlight(
            flightIdFactory.flightId(c.flightId()),
            iataAirportCodeFactory.code(c.origin()),
            iataAirportCodeFactory.code(c.destination()),
            c.departureTime(),
            c.departureDays()
        )
    );
    return this;
  }

}

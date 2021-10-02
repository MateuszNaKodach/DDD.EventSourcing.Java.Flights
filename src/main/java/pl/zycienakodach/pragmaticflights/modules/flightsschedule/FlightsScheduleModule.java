package pl.zycienakodach.pragmaticflights.modules.flightsschedule;

import pl.zycienakodach.pragmaticflights.modules.flightsschedule.api.commands.ScheduleFlightCourses;
import pl.zycienakodach.pragmaticflights.sdk.Application;
import pl.zycienakodach.pragmaticflights.sdk.ApplicationModule;
import pl.zycienakodach.pragmaticflights.sdk.application.EventStreamName;
import pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.flightid.FlightIdFactory;
import pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.iata.IATAAirportCodeFactory;
import pl.zycienakodach.pragmaticflights.sdk.application.time.TimeProvider;
import pl.zycienakodach.pragmaticflights.sdk.domain.LocalDateRange;

import java.time.LocalDate;
import java.time.ZoneId;

import static pl.zycienakodach.pragmaticflights.modules.flightsschedule.domain.FlightScheduling.scheduleFlightCourses;
import static pl.zycienakodach.pragmaticflights.sdk.application.EventStreamName.category;
import static pl.zycienakodach.pragmaticflights.sdk.application.EventStreamName.streamId;

public class FlightsScheduleModule implements ApplicationModule {

  private final TimeProvider timeProvider;
  private final FlightIdFactory flightIdFactory;
  private final IATAAirportCodeFactory iataAirportCodeFactory;

  public FlightsScheduleModule(TimeProvider timeProvider, FlightIdFactory flightIdFactory, IATAAirportCodeFactory iataAirportCodeFactory) {
    this.timeProvider = timeProvider;
    this.flightIdFactory = flightIdFactory;
    this.iataAirportCodeFactory = iataAirportCodeFactory;
  }

  @Override
  public ApplicationModule configure(Application app) {
    app.onCommand(
        ScheduleFlightCourses.class,
        (c,m) -> new EventStreamName(category(m.tenantId().raw(), "FlightSchedule"), streamId(c.flightId())),
        (c) -> scheduleFlightCourses(
            flightIdFactory.flightId(c.flightId()),
            iataAirportCodeFactory.code(c.origin()),
            iataAirportCodeFactory.code(c.destination()),
            c.departureTime(),
            c.departureDays(),
            LocalDateRange.future(LocalDate.ofInstant(timeProvider.get(), ZoneId.of("UTC")), c.fromDate(), c.toDate())
        )
    );
    return this;
  }

}

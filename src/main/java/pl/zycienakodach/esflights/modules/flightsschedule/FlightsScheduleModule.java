package pl.zycienakodach.esflights.modules.flightsschedule;

import pl.zycienakodach.esflights.modules.flightsschedule.api.commands.ScheduleFlightCourses;
import pl.zycienakodach.esflights.sdk.Application;
import pl.zycienakodach.esflights.sdk.ApplicationModule;
import pl.zycienakodach.esflights.sdk.application.eventstream.EventStreamName;
import pl.zycienakodach.esflights.modules.sharedkernel.domain.flightid.FlightIdFactory;
import pl.zycienakodach.esflights.modules.sharedkernel.domain.iata.IATAAirportCodeFactory;
import pl.zycienakodach.esflights.sdk.application.time.TimeProvider;
import pl.zycienakodach.esflights.sdk.domain.LocalDateRange;

import java.time.LocalDate;
import java.time.ZoneId;

import static pl.zycienakodach.esflights.modules.flightsschedule.domain.FlightScheduling.scheduleFlightCourses;

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
        (c, m) -> EventStreamName.ofCategory("FlightSchedule").withId(c.flightId()),
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

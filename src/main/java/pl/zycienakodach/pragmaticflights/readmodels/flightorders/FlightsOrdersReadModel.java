package pl.zycienakodach.pragmaticflights.readmodels.flightorders;

import pl.zycienakodach.pragmaticflights.modules.ordering.api.events.FlightsOrderSubmitted;
import pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.flightid.FlightCourseId;
import pl.zycienakodach.pragmaticflights.readmodels.flightorders.api.FlightOrdersRepository;
import pl.zycienakodach.pragmaticflights.sdk.Application;
import pl.zycienakodach.pragmaticflights.sdk.ApplicationModule;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;

public class FlightsOrdersReadModel implements ApplicationModule {

  private final FlightOrdersRepository flightOrdersRepository;

  public FlightsOrdersReadModel(FlightOrdersRepository flightOrdersRepository) {
    this.flightOrdersRepository = flightOrdersRepository;
  }

  @Override
  public ApplicationModule configure(Application app) {
    app.when(FlightsOrderSubmitted.class, (e, __) -> {
      var flightCourseId = FlightCourseId.fromRaw(e.flightCourseId());
      flightOrdersRepository.add(
          new FlightOrder(
              e.orderId(),
              e.customerId(),
              LocalDate.ofInstant(flightCourseId.departureAt(), ZoneId.of("UTC")),
              new FlightOrder.Flight(
                  flightCourseId.flightId().raw(),
                  e.origin(),
                  e.destination(),
                  LocalTime.ofInstant(flightCourseId.departureAt(), ZoneId.of("UTC"))
              )
          )
      );
    });
    return this;
  }
}

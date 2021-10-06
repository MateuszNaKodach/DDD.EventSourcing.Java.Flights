package pl.zycienakodach.pragmaticflights.modules.discounts.infrastructure.flightorders;

import pl.zycienakodach.pragmaticflights.modules.ordering.api.events.FlightOrderSubmitted;
import pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.flightid.FlightCourseId;
import pl.zycienakodach.pragmaticflights.sdk.Application;
import pl.zycienakodach.pragmaticflights.sdk.ApplicationModule;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;

public class FlightsOrdersProjection implements ApplicationModule {

  private final FlightOrdersRepository flightOrdersRepository;

  public FlightsOrdersProjection(FlightOrdersRepository flightOrdersRepository) {
    this.flightOrdersRepository = flightOrdersRepository;
  }

  @Override
  public ApplicationModule configure(Application app) {
    app.when(FlightOrderSubmitted.class, (e, __) -> {
      var flightCourseId = FlightCourseId.fromRaw(e.flightCourseId());
      flightOrdersRepository.add(
          new FlightOrderEntity(
              e.orderId(),
              e.customerId(),
              LocalDate.ofInstant(flightCourseId.departureAt(), ZoneId.of("UTC")),
              new FlightOrderEntity.Flight(
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

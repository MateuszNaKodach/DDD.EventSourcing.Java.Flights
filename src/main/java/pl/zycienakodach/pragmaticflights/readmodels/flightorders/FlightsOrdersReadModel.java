package pl.zycienakodach.pragmaticflights.readmodels.flightorders;

import pl.zycienakodach.pragmaticflights.modules.ordering.api.events.FlightsOrderSubmitted;
import pl.zycienakodach.pragmaticflights.readmodels.flightorders.api.FlightOrdersRepository;
import pl.zycienakodach.pragmaticflights.sdk.Application;
import pl.zycienakodach.pragmaticflights.sdk.ApplicationModule;

public class FlightsOrdersReadModel implements ApplicationModule {

  private final FlightOrdersRepository flightOrdersRepository;

  public FlightsOrdersReadModel(FlightOrdersRepository flightOrdersRepository) {
    this.flightOrdersRepository = flightOrdersRepository;
  }

  @Override
  public ApplicationModule configure(Application app) {
    app.when(FlightsOrderSubmitted.class, (e, m) -> {
      flightOrdersRepository.add(
          new FlightOrder(
              e.orderId(),
              e.customerId(),
              e.flightDate(),
              new FlightOrder.Flight(
                  e.flightId(),
                  e.origin(),
                  e.destination(),
                  e.departureTime()
              )
          )
      );
    });
    return this;
  }
}

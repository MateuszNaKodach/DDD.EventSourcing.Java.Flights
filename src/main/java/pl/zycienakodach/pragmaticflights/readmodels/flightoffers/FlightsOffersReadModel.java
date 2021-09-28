package pl.zycienakodach.pragmaticflights.readmodels.flightoffers;

import pl.zycienakodach.pragmaticflights.modules.ordering.api.events.FlightOfferedForSell;
import pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.flightid.FlightId;
import pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.iata.IATAAirportCode;
import pl.zycienakodach.pragmaticflights.readmodels.flightoffers.api.FlightOffersRepository;
import pl.zycienakodach.pragmaticflights.sdk.Application;
import pl.zycienakodach.pragmaticflights.sdk.ApplicationModule;

public class FlightsOffersReadModel implements ApplicationModule {

  private final FlightOffersRepository flightOffersRepository;

  public FlightsOffersReadModel(FlightOffersRepository flightOffersRepository) {
    this.flightOffersRepository = flightOffersRepository;
  }

  @Override
  public ApplicationModule configure(Application app) {
    app.when(FlightOfferedForSell.class, (e, m) -> {
      flightOffersRepository.add(
          new FlightOffer(
              FlightId.fromRaw(e.flightId()),
              IATAAirportCode.fromRaw(e.origin()),
              IATAAirportCode.fromRaw(e.destination()),
              e.departureTime(),
              e.departureDays()
          )
      );
    });
    return this;
  }
}

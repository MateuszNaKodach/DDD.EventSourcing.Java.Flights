package pl.zycienakodach.pragmaticflights.modules.ordering.infrastructure.offers;

import pl.zycienakodach.pragmaticflights.modules.ordering.api.events.FlightOfferedForSell;
import pl.zycienakodach.pragmaticflights.modules.ordering.domain.FlightOffer;
import pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.flightid.FlightId;
import pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.iata.IATAAirportCode;
import pl.zycienakodach.pragmaticflights.sdk.Application;

class FlightsOffersProjector {

  private final FlightOffersRepository flightOffersRepository;

  FlightsOffersProjector(FlightOffersRepository flightOffersRepository) {
    this.flightOffersRepository = flightOffersRepository;
  }

  void project(Application app) {
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
  }

}

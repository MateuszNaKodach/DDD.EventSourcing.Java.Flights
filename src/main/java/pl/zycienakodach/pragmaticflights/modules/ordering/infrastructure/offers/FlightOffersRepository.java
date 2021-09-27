package pl.zycienakodach.pragmaticflights.modules.ordering.infrastructure.offers;

import pl.zycienakodach.pragmaticflights.modules.ordering.application.FlightsOffers;
import pl.zycienakodach.pragmaticflights.modules.ordering.domain.FlightOffer;

interface FlightOffersRepository extends FlightsOffers {
  void add(FlightOffer flightOffer);
}

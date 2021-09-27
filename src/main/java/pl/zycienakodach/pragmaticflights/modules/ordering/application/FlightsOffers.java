package pl.zycienakodach.pragmaticflights.modules.ordering.application;

import pl.zycienakodach.pragmaticflights.readmodels.flightoffers.FlightOffer;
import pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.flightid.FlightId;

import java.util.Optional;

public interface FlightsOffers {
  Optional<FlightOffer> findBy(FlightId flightId);
}

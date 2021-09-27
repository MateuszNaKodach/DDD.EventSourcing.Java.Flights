package pl.zycienakodach.pragmaticflights.readmodels.flightoffers.api;

import pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.flightid.FlightId;
import pl.zycienakodach.pragmaticflights.readmodels.flightoffers.FlightOffer;

import java.util.Optional;

public interface FlightOffersRepository {
  void add(FlightOffer flightOffer);

  Optional<FlightOffer> findBy(FlightId flightId);
}

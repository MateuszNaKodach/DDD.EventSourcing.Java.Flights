package pl.zycienakodach.pragmaticflights.modules.ordering.application;

import pl.zycienakodach.pragmaticflights.modules.ordering.domain.FlightOffer;
import pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.flightid.FlightId;

import java.time.LocalDate;
import java.util.Optional;

public interface FlightsOffers {
  Optional<FlightOffer> findBy(FlightId flightId, LocalDate departureDay);
}

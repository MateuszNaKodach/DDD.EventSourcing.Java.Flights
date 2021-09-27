package pl.zycienakodach.pragmaticflights.modules.ordering.infrastructure;

import pl.zycienakodach.pragmaticflights.modules.ordering.application.FlightsOffers;
import pl.zycienakodach.pragmaticflights.readmodels.flightoffers.FlightOffer;
import pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.flightid.FlightId;
import pl.zycienakodach.pragmaticflights.readmodels.flightoffers.api.FlightOffersRepository;

import java.util.Optional;

public class FlightOffersReadModelAdapter implements FlightsOffers {

  private final FlightOffersRepository repository;

  public FlightOffersReadModelAdapter(FlightOffersRepository repository) {
    this.repository = repository;
  }

  @Override
  public Optional<FlightOffer> findBy(FlightId flightId) {
    return this.repository.findBy(flightId);
  }

}

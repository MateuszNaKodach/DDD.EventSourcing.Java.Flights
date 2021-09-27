package pl.zycienakodach.pragmaticflights.readmodels.flightoffers.infrastructure;

import pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.flightid.FlightId;
import pl.zycienakodach.pragmaticflights.readmodels.flightoffers.FlightOffer;
import pl.zycienakodach.pragmaticflights.readmodels.flightoffers.api.FlightOffersRepository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryFlightOffers implements FlightOffersRepository {

  private final ConcurrentHashMap<FlightId, FlightOffer> entities = new ConcurrentHashMap<>();

  @Override
  public void add(FlightOffer flightOffer) {
    entities.put(flightOffer.flightId(), flightOffer);
  }

  @Override
  public Optional<FlightOffer> findBy(FlightId flightId) {
    if (!entities.containsKey(flightId)) {
      return Optional.empty();
    }
    return Optional.of(entities.get(flightId));
  }
}

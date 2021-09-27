package pl.zycienakodach.pragmaticflights.modules.ordering.infrastructure.offers;

import pl.zycienakodach.pragmaticflights.modules.ordering.domain.FlightOffer;
import pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.flightid.FlightId;

import java.time.LocalDate;
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

package pl.zycienakodach.pragmaticflights.modules.ordering.infrastructure.offers;

import pl.zycienakodach.pragmaticflights.modules.ordering.domain.FlightOffer;
import pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.flightid.FlightId;

import java.time.LocalDate;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

class InMemoryFlightOffers implements FlightOffersRepository {

  private final ConcurrentHashMap<FlightId, FlightOffer> entities = new ConcurrentHashMap<>();

  @Override
  public void add(FlightOffer flightOffer) {
    entities.put(flightOffer.flightId(), flightOffer);
  }

  @Override
  public Optional<FlightOffer> findBy(FlightId flightId, LocalDate departureDay) {
    if (!entities.containsKey(flightId)) {
      return Optional.empty();
    }
    var found = entities.get(flightId);
    var departureOnSelectedDay = found.departureDays().stream().anyMatch(d -> d.equals(departureDay.getDayOfWeek()));
    return departureOnSelectedDay ? Optional.of(found) : Optional.empty();
  }
}

package pl.zycienakodach.pragmaticflights.readmodels.regularprices.infrastructure;

import pl.zycienakodach.pragmaticflights.readmodels.regularprices.api.FlightRegularPriceRepository;

import java.time.DayOfWeek;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryFlightRegularPriceRepository implements FlightRegularPriceRepository {

  private final ConcurrentHashMap<Key, Double> entities = new ConcurrentHashMap<>();

  @Override
  public void save(String flightId, DayOfWeek dayOfWeek, double priceInEuro) {
    entities.put(new Key(flightId, dayOfWeek), priceInEuro);
  }

  @Override
  public Optional<Double> findBy(String flightId, DayOfWeek dayOfWeek) {
    return Optional.ofNullable(entities.getOrDefault(new Key(flightId, dayOfWeek), null));
  }

  private static record Key(String flightId, DayOfWeek dayOfWeek) {
  }
}

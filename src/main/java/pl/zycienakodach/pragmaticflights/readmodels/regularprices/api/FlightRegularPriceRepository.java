package pl.zycienakodach.pragmaticflights.readmodels.regularprices.api;

import java.time.DayOfWeek;
import java.util.Optional;

public interface FlightRegularPriceRepository {

  void save(String flightId, DayOfWeek dayOfWeek, double priceInEuro);

  Optional<Double> findBy(String flightId, DayOfWeek dayOfWeek);

}

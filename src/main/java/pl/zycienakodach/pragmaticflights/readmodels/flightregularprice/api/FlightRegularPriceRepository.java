package pl.zycienakodach.pragmaticflights.readmodels.flightregularprice.api;

import java.time.DayOfWeek;

interface FlightRegularPriceRepository {

  void save(String flightId, DayOfWeek dayOfWeek, double priceInEuro);

  double findBy(String flightId, DayOfWeek dayOfWeek);

}

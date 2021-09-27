package pl.zycienakodach.pragmaticflights.readmodels.flightorders.api;

import pl.zycienakodach.pragmaticflights.readmodels.flightorders.FlightOrder;

import java.util.Optional;

public interface FlightOrdersRepository {

  void add(FlightOrder flightOrder);

  Optional<FlightOrder> findBy(String orderId);

}

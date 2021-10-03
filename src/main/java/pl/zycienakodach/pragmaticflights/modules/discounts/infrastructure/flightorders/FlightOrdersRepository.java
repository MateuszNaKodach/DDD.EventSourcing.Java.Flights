package pl.zycienakodach.pragmaticflights.modules.discounts.infrastructure.flightorders;

import java.util.Optional;

public interface FlightOrdersRepository {

  void add(FlightOrder flightOrder);

  Optional<FlightOrder> findBy(String orderId);

}

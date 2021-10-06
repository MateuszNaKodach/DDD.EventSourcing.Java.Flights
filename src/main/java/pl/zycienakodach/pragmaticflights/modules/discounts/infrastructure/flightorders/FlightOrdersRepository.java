package pl.zycienakodach.pragmaticflights.modules.discounts.infrastructure.flightorders;

import java.util.Optional;

public interface FlightOrdersRepository {

  void add(FlightOrderEntity flightOrder);

  Optional<FlightOrderEntity> findBy(String orderId);

}

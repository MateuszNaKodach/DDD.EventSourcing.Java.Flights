package pl.zycienakodach.pragmaticflights.modules.discounts.infrastructure.flightorders;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryFlightOrders implements FlightOrdersRepository {

  private final ConcurrentHashMap<String, FlightOrderEntity> entities = new ConcurrentHashMap<>();

  @Override
  public void add(FlightOrderEntity flightOrder) {
    entities.put(flightOrder.orderId(), flightOrder);
  }

  @Override
  public Optional<FlightOrderEntity> findBy(String orderId) {
    if (!entities.containsKey(orderId)) {
      return Optional.empty();
    }
    return Optional.of(entities.get(orderId));
  }
}

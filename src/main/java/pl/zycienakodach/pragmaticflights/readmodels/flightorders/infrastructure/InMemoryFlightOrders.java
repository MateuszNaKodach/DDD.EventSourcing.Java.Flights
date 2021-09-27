package pl.zycienakodach.pragmaticflights.readmodels.flightorders.infrastructure;

import pl.zycienakodach.pragmaticflights.readmodels.flightorders.FlightOrder;
import pl.zycienakodach.pragmaticflights.readmodels.flightorders.api.FlightOrdersRepository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryFlightOrders implements FlightOrdersRepository {

  private final ConcurrentHashMap<String, FlightOrder> entities = new ConcurrentHashMap<>();

  @Override
  public void add(FlightOrder flightOrder) {
    entities.put(flightOrder.orderId(), flightOrder);
  }

  @Override
  public Optional<FlightOrder> findBy(String orderId) {
    if (!entities.containsKey(orderId)) {
      return Optional.empty();
    }
    return Optional.of(entities.get(orderId));
  }
}

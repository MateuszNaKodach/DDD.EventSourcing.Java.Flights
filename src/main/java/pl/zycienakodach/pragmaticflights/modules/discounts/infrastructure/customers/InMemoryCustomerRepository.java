package pl.zycienakodach.pragmaticflights.modules.discounts.infrastructure.customers;

import pl.zycienakodach.pragmaticflights.modules.discounts.domain.criterias.flightdepartureoncustomerbirthday.CustomersBirthdays;
import pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.customerid.CustomerId;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

public class InMemoryCustomerRepository implements CustomersBirthdays {

  private final ConcurrentHashMap<String, CustomerEntity> entities = new ConcurrentHashMap<>();

  public InMemoryCustomerRepository(Set<CustomerEntity> customers) {
    var map = customers.stream().collect(Collectors.toMap(CustomerEntity::customerId, Function.identity()));
    entities.putAll(map);
  }

  @Override
  public Optional<LocalDate> forCustomer(CustomerId customerId) {
    if (!entities.containsKey(customerId.raw())) {
      return Optional.empty();
    }

    return Optional.of(entities.get(customerId.raw()))
        .map(CustomerEntity::dateOfBirth);
  }
}

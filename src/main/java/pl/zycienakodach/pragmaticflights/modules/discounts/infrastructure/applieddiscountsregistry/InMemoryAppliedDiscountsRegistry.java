package pl.zycienakodach.pragmaticflights.modules.discounts.infrastructure.applieddiscountsregistry;

import pl.zycienakodach.pragmaticflights.modules.discounts.application.AppliedDiscountsRegistry;
import pl.zycienakodach.pragmaticflights.modules.discounts.domain.criterias.OrderDiscountCriteria;
import pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.orderid.OrderId;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryAppliedDiscountsRegistry implements AppliedDiscountsRegistry {

  private final ConcurrentHashMap<OrderId, Set<OrderDiscountCriteria>> entities = new ConcurrentHashMap<>();

  @Override
  public void save(OrderId orderId, Set<OrderDiscountCriteria> appliedCriteria) {
    entities.put(orderId, appliedCriteria);
  }
}

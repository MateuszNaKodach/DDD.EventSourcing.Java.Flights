package pl.zycienakodach.esflights.modules.discounts.infrastructure.applieddiscountsregistry;

import pl.zycienakodach.esflights.modules.discounts.application.AppliedDiscountsRegistry;
import pl.zycienakodach.esflights.modules.discounts.domain.criterias.DiscountCriteriaName;
import pl.zycienakodach.esflights.modules.sharedkernel.domain.orderid.OrderId;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryAppliedDiscountsRegistry implements AppliedDiscountsRegistry {

  private final ConcurrentHashMap<OrderId, List<DiscountCriteriaName>> entities = new ConcurrentHashMap<>();

  @Override
  public void save(OrderId orderId, List<DiscountCriteriaName> appliedCriteria) {
    entities.put(orderId, appliedCriteria);
  }
}

package pl.zycienakodach.pragmaticflights.modules.discounts.application;

import pl.zycienakodach.pragmaticflights.modules.discounts.domain.criterias.DiscountCriteriaName;
import pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.orderid.OrderId;

import java.util.Set;

public interface AppliedDiscountsRegistry {

  void save(OrderId orderId, Set<DiscountCriteriaName> appliedCriteria);

}

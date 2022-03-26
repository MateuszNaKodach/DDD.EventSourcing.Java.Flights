package pl.zycienakodach.esflights.modules.discounts.application;

import pl.zycienakodach.esflights.modules.discounts.domain.criterias.DiscountCriteriaName;
import pl.zycienakodach.esflights.modules.sharedkernel.domain.orderid.OrderId;

import java.util.List;

public interface AppliedDiscountsRegistry {

  void save(OrderId orderId, List<DiscountCriteriaName> appliedCriteria);

}

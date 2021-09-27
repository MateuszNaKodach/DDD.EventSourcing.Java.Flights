package pl.zycienakodach.pragmaticflights.modules.discounts.domain.criterias;

import pl.zycienakodach.pragmaticflights.modules.discounts.domain.DiscountValue;
import pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.orderid.OrderId;

import java.util.Optional;

public interface OrderDiscountCriteria {

  // pass as argument - regularPrice, already applied criteria etc. Return Discount{value, appliedCriteria}
  Optional<DiscountValue> discountValue(OrderId orderId);
}

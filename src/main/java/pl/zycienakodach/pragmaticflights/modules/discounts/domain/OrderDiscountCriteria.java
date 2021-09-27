package pl.zycienakodach.pragmaticflights.modules.discounts.domain;

import pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.orderid.OrderId;

import java.util.Optional;

public interface OrderDiscountCriteria {
  Optional<DiscountValue> discountValue(OrderId orderId);
}

package pl.zycienakodach.esflights.modules.discounts.domain.criterias;

import pl.zycienakodach.esflights.modules.discounts.domain.Discount;
import pl.zycienakodach.esflights.modules.sharedkernel.domain.orderid.OrderId;

public interface OrderDiscountCriteria {

  Discount calculateDiscount(OrderId orderId);
}

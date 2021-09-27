package pl.zycienakodach.pragmaticflights.modules.discounts.domain.criterias;

import pl.zycienakodach.pragmaticflights.modules.discounts.domain.Discount;
import pl.zycienakodach.pragmaticflights.modules.discounts.domain.RegularPrice;
import pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.orderid.OrderId;

public interface OrderDiscountCriteria {

  public Discount calculateDiscount(OrderId orderId, RegularPrice regularPrice);
}

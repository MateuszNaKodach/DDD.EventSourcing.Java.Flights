package pl.zycienakodach.pragmaticflights.modules.discounts.domain.criterias.flightdepartureoncustomerbirthday;

import pl.zycienakodach.pragmaticflights.modules.discounts.domain.Discount;
import pl.zycienakodach.pragmaticflights.modules.discounts.domain.RegularPrice;
import pl.zycienakodach.pragmaticflights.modules.discounts.domain.criterias.OrderDiscountCriteria;
import pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.orderid.OrderId;

public class FlightDepartureOnCustomerBirthdayDiscount implements OrderDiscountCriteria {

  @Override
  public Discount calculateDiscount(OrderId orderId, RegularPrice regularPrice) {
    return Discount.none();
  }
}

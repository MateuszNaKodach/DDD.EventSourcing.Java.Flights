package pl.zycienakodach.pragmaticflights.modules.discounts.domain.criterias.flightdepartureoncustomerbirthday;

import pl.zycienakodach.pragmaticflights.modules.discounts.domain.DiscountValue;
import pl.zycienakodach.pragmaticflights.modules.discounts.domain.criterias.OrderDiscountCriteria;
import pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.orderid.OrderId;

import java.util.Optional;

class FlightDepartureOnCustomerBirthdayDiscount implements OrderDiscountCriteria {

  @Override
  public Optional<DiscountValue> discountValue(OrderId orderId) {
    return Optional.empty();
  }
}

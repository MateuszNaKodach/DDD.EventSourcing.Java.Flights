package pl.zycienakodach.pragmaticflights.modules.discounts.domain.criterias;

import pl.zycienakodach.pragmaticflights.modules.discounts.domain.DiscountValue;
import pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.orderid.OrderId;

import java.util.Optional;

class PossibleDiscounts implements OrderDiscountCriteria {

//  private

  @Override
  public Optional<DiscountValue> discountValue(OrderId orderId) {
    return Optional.empty();
  }
}

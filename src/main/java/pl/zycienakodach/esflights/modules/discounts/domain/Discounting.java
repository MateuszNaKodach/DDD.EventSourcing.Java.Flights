package pl.zycienakodach.esflights.modules.discounts.domain;

import pl.zycienakodach.esflights.modules.discounts.api.event.DiscountValueCalculated;
import pl.zycienakodach.esflights.modules.discounts.api.event.DiscountsEvent;
import pl.zycienakodach.esflights.modules.sharedkernel.domain.orderid.OrderId;
import pl.zycienakodach.esflights.sdk.domain.DomainLogic;

import java.util.List;

public class Discounting {

  public static DomainLogic<DiscountsEvent> calculateDiscount(
      OrderId orderId,
      Discount discount
  ) {
    return (List<DiscountsEvent> pastEvents) -> {
      var discountValueCalculated = new DiscountValueCalculated(
          orderId.raw(),
          discount.euro().toBigDecimal()
      );
      return List.of(discountValueCalculated);
    };
  }


}

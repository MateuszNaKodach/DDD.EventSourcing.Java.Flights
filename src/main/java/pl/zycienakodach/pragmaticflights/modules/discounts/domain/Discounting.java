package pl.zycienakodach.pragmaticflights.modules.discounts.domain;

import pl.zycienakodach.pragmaticflights.modules.discounts.api.event.DiscountValueCalculated;
import pl.zycienakodach.pragmaticflights.modules.discounts.api.event.DiscountsEvent;
import pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.orderid.OrderId;
import pl.zycienakodach.pragmaticflights.sdk.domain.DomainLogic;

import java.util.List;

public class Discounting {

  public static DomainLogic<DiscountsEvent> calculateDiscount(
      OrderId orderId,
      Discount discount
  ) {
    return (List<DiscountsEvent> pastEvents) -> {
      var discountValueCalculated = new DiscountValueCalculated(
          orderId.raw(),
          discount.euro().value()
      );
      return List.of(discountValueCalculated);
    };
  }


}

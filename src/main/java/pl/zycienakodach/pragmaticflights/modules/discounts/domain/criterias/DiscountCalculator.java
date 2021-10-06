package pl.zycienakodach.pragmaticflights.modules.discounts.domain.criterias;

import pl.zycienakodach.pragmaticflights.modules.discounts.domain.Discount;
import pl.zycienakodach.pragmaticflights.modules.discounts.domain.RegularPrice;
import pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.money.EuroMoney;
import pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.orderid.OrderId;

import java.util.List;
import java.util.function.BiFunction;

public class DiscountCalculator {

  private final EuroMoney minimalFlightPriceWithDiscount;
  private final List<OrderDiscountCriteria> criteria;

  public DiscountCalculator(EuroMoney minimalFlightPriceWithDiscount, List<OrderDiscountCriteria> criteria) {
    this.minimalFlightPriceWithDiscount = minimalFlightPriceWithDiscount;
    this.criteria = criteria;
  }

  public Discount calculateDiscount(OrderId orderId, RegularPrice regularPrice) {
    return criteria.stream()
        .reduce(Discount.none(), applyDiscountIfKeepMinimalPrice(orderId, regularPrice), Discount::plus);
  }

  private BiFunction<Discount, OrderDiscountCriteria, Discount> applyDiscountIfKeepMinimalPrice(OrderId orderId, RegularPrice regularPrice) {
    return (currentDiscount, c) -> {
      var resultDiscount = currentDiscount.plus(c.calculateDiscount(orderId));
      return willKeepMinimalPrice(regularPrice, resultDiscount) ? resultDiscount : currentDiscount;
    };
  }

  private boolean willKeepMinimalPrice(RegularPrice regularPrice, Discount discount){
    return regularPrice.euro().subtract(discount.euro())
        .greaterOrEqual(minimalFlightPriceWithDiscount);
  }
}

package pl.zycienakodach.esflights.modules.discounts.domain.criterias;

import pl.zycienakodach.esflights.modules.discounts.domain.Discount;
import pl.zycienakodach.esflights.modules.sharedkernel.domain.money.EuroMoney;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class DiscountCalculatorTestFixtures {

  public static List<OrderDiscountCriteria> discountsCriteriaWith(Map<String, BigDecimal> amounts){
    return amounts.entrySet()
        .stream()
        .map(c -> (OrderDiscountCriteria) (orderId) -> Discount.just(new DiscountCriteriaName(c.getKey()), EuroMoney.of(c.getValue())))
        .toList();
  }

}

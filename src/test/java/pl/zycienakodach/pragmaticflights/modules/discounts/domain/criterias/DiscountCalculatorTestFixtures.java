package pl.zycienakodach.pragmaticflights.modules.discounts.domain.criterias;

import pl.zycienakodach.pragmaticflights.modules.discounts.domain.Discount;
import pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.money.EuroMoney;

import java.util.List;

public class DiscountCalculatorTestFixtures {

  public static List<OrderDiscountCriteria> discountsCriteriaWith(List<Double> amounts){
    return amounts.stream()
        .map(amount -> (OrderDiscountCriteria) (orderId, regularPrice) -> Discount.just(new DiscountCriteriaName("Discount"), EuroMoney.of(amount)))
        .toList();
  }

}

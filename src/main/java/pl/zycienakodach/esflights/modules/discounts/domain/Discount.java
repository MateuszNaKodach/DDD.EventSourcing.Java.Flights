package pl.zycienakodach.esflights.modules.discounts.domain;

import pl.zycienakodach.esflights.modules.discounts.domain.criterias.DiscountCriteriaName;
import pl.zycienakodach.esflights.modules.sharedkernel.domain.money.EuroMoney;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public record Discount(List<DiscountCriteriaName> appliedCriteria, EuroMoney euro) {

  public static Discount just(DiscountCriteriaName criteria, EuroMoney euro) {
    return new Discount(List.of(criteria), euro);
  }

  public static Discount none() {
    return new Discount(Collections.emptyList(), EuroMoney.zero());
  }

  public Discount plus(Discount discount) {
    List<DiscountCriteriaName> appliedCriteria = Stream.concat(this.appliedCriteria.stream(), discount.appliedCriteria.stream()).toList();
    return new Discount(appliedCriteria, this.euro.add(discount.euro));
  }
}

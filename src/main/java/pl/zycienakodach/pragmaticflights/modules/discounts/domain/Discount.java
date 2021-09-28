package pl.zycienakodach.pragmaticflights.modules.discounts.domain;

import pl.zycienakodach.pragmaticflights.modules.discounts.domain.criterias.DiscountCriteriaName;
import pl.zycienakodach.pragmaticflights.modules.discounts.domain.criterias.OrderDiscountCriteria;
import pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.money.EuroMoney;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public record Discount(Set<DiscountCriteriaName> appliedCriteria, EuroMoney euro) {

  public static Discount just(DiscountCriteriaName criteria, EuroMoney euro) {
    return new Discount(Set.of(criteria), euro);
  }

  public static Discount none() {
    return new Discount(Collections.emptySet(), EuroMoney.zero());
  }

  public Discount plus(Discount discount) {
    Set<DiscountCriteriaName> appliedCriteria = Stream.concat(this.appliedCriteria.stream(), discount.appliedCriteria.stream()).collect(Collectors.toSet());
    return new Discount(appliedCriteria, this.euro.plus(discount.euro));
  }
}

package pl.zycienakodach.pragmaticflights.modules.discounts.domain.criterias;

public record DiscountCriteriaName(String raw) {

  public static DiscountCriteriaName of(String name) {
    return new DiscountCriteriaName(name);
  }

}

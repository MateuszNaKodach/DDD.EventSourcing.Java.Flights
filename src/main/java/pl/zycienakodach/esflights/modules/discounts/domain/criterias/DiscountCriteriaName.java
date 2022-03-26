package pl.zycienakodach.esflights.modules.discounts.domain.criterias;

public record DiscountCriteriaName(String raw) {

  public static DiscountCriteriaName of(String name) {
    return new DiscountCriteriaName(name);
  }

}

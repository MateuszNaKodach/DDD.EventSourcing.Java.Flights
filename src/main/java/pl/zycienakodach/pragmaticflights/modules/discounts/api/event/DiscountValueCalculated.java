package pl.zycienakodach.pragmaticflights.modules.discounts.api.event;

public record DiscountValueCalculated(String orderId, double discountInEuro) implements DiscountsEvent {
}

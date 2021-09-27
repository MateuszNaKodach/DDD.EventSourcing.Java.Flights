package pl.zycienakodach.pragmaticflights.modules.pricing.api.events;

public record DiscountApplied(String orderId, double discountInEuro) implements PricingEvent {
}

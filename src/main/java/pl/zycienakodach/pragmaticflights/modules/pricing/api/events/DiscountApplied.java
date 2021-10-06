package pl.zycienakodach.pragmaticflights.modules.pricing.api.events;

import java.math.BigDecimal;

public record DiscountApplied(String orderId, BigDecimal discountInEuro) implements PricingEvent {
}

package pl.zycienakodach.pragmaticflights.modules.pricing.api.events;

import java.math.BigDecimal;

public record CalculateOrderTotalPriceCompleted(
    String orderId, BigDecimal regularPriceInEuro, BigDecimal discountPriceInEuro, BigDecimal totalPriceInEuro
) implements PricingEvent {
}

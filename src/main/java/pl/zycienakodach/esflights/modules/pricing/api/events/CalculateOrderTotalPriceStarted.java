package pl.zycienakodach.esflights.modules.pricing.api.events;

import java.math.BigDecimal;

public record CalculateOrderTotalPriceStarted(
    String orderId, BigDecimal regularPriceInEuro
) implements PricingEvent {
}

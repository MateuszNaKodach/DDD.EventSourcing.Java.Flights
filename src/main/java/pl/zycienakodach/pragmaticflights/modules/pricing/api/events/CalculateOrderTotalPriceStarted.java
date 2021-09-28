package pl.zycienakodach.pragmaticflights.modules.pricing.api.events;

public record CalculateOrderTotalPriceStarted(
    String orderId, double regularPriceInEuro
) implements PricingEvent {
}

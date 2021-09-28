package pl.zycienakodach.pragmaticflights.modules.pricing.api.events;

public record CalculateOrderTotalPriceCompleted(
    String orderId, double regularPriceInEuro, double discountPriceInEuro, double totalPriceInEuro
) implements PricingEvent {
}

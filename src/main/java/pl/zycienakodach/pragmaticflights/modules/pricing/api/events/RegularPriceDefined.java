package pl.zycienakodach.pragmaticflights.modules.pricing.api.events;

public record RegularPriceDefined(String flightCourseId, double priceInEuro) implements PricingEvent {
}

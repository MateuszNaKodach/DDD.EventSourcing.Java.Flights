package pl.zycienakodach.pragmaticflights.modules.pricing.api;

import java.time.DayOfWeek;

public record RegularPriceDefined(String flightId, DayOfWeek dayOfWeek, double priceInEuro) implements PricingEvent {
}

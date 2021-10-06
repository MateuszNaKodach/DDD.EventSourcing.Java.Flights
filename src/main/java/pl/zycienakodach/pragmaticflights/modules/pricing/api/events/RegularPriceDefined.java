package pl.zycienakodach.pragmaticflights.modules.pricing.api.events;

import java.math.BigDecimal;

public record RegularPriceDefined(String flightCourseId, BigDecimal priceInEuro) implements PricingEvent {
}

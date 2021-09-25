package pl.zycienakodach.pragmaticflights.modules.pricing.api;

import java.time.DayOfWeek;

public record DefineDiscount(String flightId, DayOfWeek dayOfWeek, double priceInEuro) {
}

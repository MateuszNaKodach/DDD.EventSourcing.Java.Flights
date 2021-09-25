package pl.zycienakodach.pragmaticflights.pricing.api;

import java.time.DayOfWeek;

public record DefineDiscount(String flightId, DayOfWeek dayOfWeek, double priceInEuro) {
}

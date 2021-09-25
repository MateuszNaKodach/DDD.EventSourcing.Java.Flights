package pl.zycienakodach.pragmaticflights.modules.pricing.api;

import java.time.DayOfWeek;

public record DefineFlightRegularPrice(String flightId, DayOfWeek dayOfWeek, double priceInEuro) {
}

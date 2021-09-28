package pl.zycienakodach.pragmaticflights.modules.pricing.api.commands;

import java.time.DayOfWeek;

public record DefineRegularPrice(
    String flightId,
    DayOfWeek dayOfWeek,
    double priceInEuro
) {
}

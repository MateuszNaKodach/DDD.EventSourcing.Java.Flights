package pl.zycienakodach.pragmaticflights.pricing.api;

import java.time.DayOfWeek;

public record DefineFlightPrice(String flightId, DayOfWeek dayOfWeek, int euroCents) {
}

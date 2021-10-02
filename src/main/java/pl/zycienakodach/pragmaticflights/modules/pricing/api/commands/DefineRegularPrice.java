package pl.zycienakodach.pragmaticflights.modules.pricing.api.commands;

public record DefineRegularPrice(
    String flightCourseId,
    double priceInEuro
) {
}

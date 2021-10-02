package pl.zycienakodach.pragmaticflights.modules.ordering.api.commands;

public record OfferFlightCourseForSell(
    String flightCourseId,
    String origin,
    String destination
) {
}

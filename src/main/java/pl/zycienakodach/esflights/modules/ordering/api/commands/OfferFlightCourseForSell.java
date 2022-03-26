package pl.zycienakodach.esflights.modules.ordering.api.commands;

public record OfferFlightCourseForSell(
    String flightCourseId,
    String origin,
    String destination
) {
}

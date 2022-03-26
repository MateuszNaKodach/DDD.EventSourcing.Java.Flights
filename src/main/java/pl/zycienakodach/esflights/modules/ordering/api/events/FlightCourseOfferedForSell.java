package pl.zycienakodach.esflights.modules.ordering.api.events;

public record FlightCourseOfferedForSell(
    String flightCourseId,
    String origin,
    String destination
) implements OrderingEvents {
}

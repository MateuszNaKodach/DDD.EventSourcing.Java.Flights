package pl.zycienakodach.pragmaticflights.modules.ordering.api.events;

public record FlightCourseOfferedForSell(
    String flightCourseId,
    String origin,
    String destination
) implements OrderingEvents {
}

package pl.zycienakodach.pragmaticflights.modules.ordering.api.events;

public record FlightOrderSubmitted(
    String orderId,
    String customerId,
    String flightCourseId,
    String origin,
    String destination
) implements OrderingEvents {

}

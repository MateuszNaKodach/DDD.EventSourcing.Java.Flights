package pl.zycienakodach.pragmaticflights.modules.ordering.api.commands;

public record SubmitFlightOrder(
    String customerId,
    String flightCourseId
) {
}

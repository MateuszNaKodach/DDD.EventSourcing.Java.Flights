package pl.zycienakodach.esflights.modules.ordering.api.commands;

public record SubmitFlightOrder(
    String customerId,
    String flightCourseId
) {
}

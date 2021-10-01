package pl.zycienakodach.pragmaticflights.processes.calculatingorderprice

import pl.zycienakodach.pragmaticflights.modules.discounts.api.event.DiscountValueCalculated
import pl.zycienakodach.pragmaticflights.modules.ordering.api.events.FlightsOrderSubmitted
import pl.zycienakodach.pragmaticflights.modules.pricing.api.commands.ApplyOrderPriceDiscount
import pl.zycienakodach.pragmaticflights.modules.pricing.api.commands.CalculateOrderTotalPrice
import pl.zycienakodach.pragmaticflights.sdk.application.EventStreamName
import pl.zycienakodach.pragmaticflights.sdk.infrastructure.message.command.InMemoryCommandBus
import pl.zycienakodach.pragmaticflights.sdk.infrastructure.message.command.RecordingCommandBus
import spock.lang.Specification

import java.time.LocalDate
import java.time.LocalTime

import static pl.zycienakodach.pragmaticflights.ApplicationTestFixtures.inMemoryApplication

class CalculatingOrderTotalPriceProcessSpec extends Specification {

    def "when flight order submitted then should calculate order total price"() {
        given:
        var commandBus = new RecordingCommandBus(new InMemoryCommandBus());
        def app = inMemoryApplication(commandBus)
                .withModule(new CalculatingOrderTotalPriceProcess())

        when:
        var eventStream = new EventStreamName("category", "id")
        def event = new FlightsOrderSubmitted(
                "orderId",
                "customerId",
                "flightId",
                LocalTime.of(23, 40),
                LocalDate.of(2021, 12, 12),
                "NYC",
                "NYC"
        )
        var eventMetadata = app.eventOccurred(
                eventStream,
                event
        )

        then:
        commandBus.lastCommandCausedBy(eventMetadata.eventId()) == new CalculateOrderTotalPrice("orderId", "flightId", LocalDate.of(2021, 12, 12))
    }

    def "when discount value calculated then should apply order price discount"() {
        given:
        var commandBus = new RecordingCommandBus(new InMemoryCommandBus());
        def app = inMemoryApplication(commandBus)
                .withModule(new CalculatingOrderTotalPriceProcess())

        when:
        var eventStream = new EventStreamName("category", "id")
        def event = new DiscountValueCalculated(
                "orderId",
                10
        )
        var eventMetadata = app.eventOccurred(
                eventStream,
                event
        )

        then:
        commandBus.lastCommandCausedBy(eventMetadata.eventId()) == new ApplyOrderPriceDiscount("orderId", 10)
    }

}

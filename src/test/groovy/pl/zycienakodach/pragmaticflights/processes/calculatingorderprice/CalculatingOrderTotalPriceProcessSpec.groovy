package pl.zycienakodach.pragmaticflights.processes.calculatingorderprice

import pl.zycienakodach.pragmaticflights.modules.discounts.api.event.DiscountValueCalculated
import pl.zycienakodach.pragmaticflights.modules.ordering.api.events.FlightsOrderSubmitted
import pl.zycienakodach.pragmaticflights.modules.pricing.api.commands.ApplyOrderPriceDiscount
import pl.zycienakodach.pragmaticflights.modules.pricing.api.commands.CalculateOrderTotalPrice
import pl.zycienakodach.pragmaticflights.sdk.infrastructure.message.command.InMemoryCommandBus
import pl.zycienakodach.pragmaticflights.sdk.infrastructure.message.command.RecordingCommandBus
import spock.lang.Specification

import static pl.zycienakodach.pragmaticflights.ApplicationTestFixtures.inMemoryApplication
import static pl.zycienakodach.pragmaticflights.ApplicationTestFixtures.test
import static pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.customerid.CustomerIdTestFixtures.rawCustomerId
import static pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.flightid.FlightCourseTestFixtures.rawFlightCourseId
import static pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.iata.IATAAirportsCodeFixtures.rawDestinationAirport
import static pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.iata.IATAAirportsCodeFixtures.rawOriginAirport
import static pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.orderid.OrderIdTestFixtures.rawOrderId

class CalculatingOrderTotalPriceProcessSpec extends Specification {

    def commandBus = new RecordingCommandBus(new InMemoryCommandBus())
    def app = test(inMemoryApplication(commandBus)
            .withModule(new CalculatingOrderTotalPriceProcess()))

    def "when flight order submitted then should calculate order total price"() {
        given:
        def orderId = rawOrderId()
        def customerId = rawCustomerId()
        def flightCourseId = rawFlightCourseId()
        def originAirport = rawOriginAirport()
        def destinationAirport = rawDestinationAirport()

        when:
        def event = new FlightsOrderSubmitted(
                orderId,
                customerId,
                flightCourseId,
                originAirport,
                destinationAirport
        )
        var eventMetadata = app.eventOccurred(event)

        then:
        commandBus.lastCommandCausedBy(eventMetadata.eventId()) == new CalculateOrderTotalPrice(orderId)
    }

    def "when discount value calculated then should apply order price discount"() {
        given:
        def orderId = rawOrderId()

        when:
        def event = new DiscountValueCalculated(
                orderId,
                10
        )
        var eventMetadata = app.eventOccurred(event)

        then:
        commandBus.lastCommandCausedBy(eventMetadata.eventId()) == new ApplyOrderPriceDiscount(orderId, 10)
    }

}

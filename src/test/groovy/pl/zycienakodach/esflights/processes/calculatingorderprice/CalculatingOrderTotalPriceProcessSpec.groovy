package pl.zycienakodach.esflights.processes.calculatingorderprice

import pl.zycienakodach.esflights.modules.discounts.api.event.DiscountValueCalculated
import pl.zycienakodach.esflights.modules.ordering.api.events.FlightOrderSubmitted
import pl.zycienakodach.esflights.modules.pricing.api.commands.ApplyOrderPriceDiscount
import pl.zycienakodach.esflights.modules.pricing.api.commands.CalculateOrderTotalPrice
import spock.lang.Specification

import static pl.zycienakodach.esflights.modules.sharedkernel.domain.customerid.CustomerIdTestFixtures.rawCustomerId
import static pl.zycienakodach.esflights.modules.sharedkernel.domain.flightid.FlightCourseTestFixtures.rawFlightCourseId
import static pl.zycienakodach.esflights.modules.sharedkernel.domain.iata.IATAAirportsCodeFixtures.rawDestinationAirport
import static pl.zycienakodach.esflights.modules.sharedkernel.domain.iata.IATAAirportsCodeFixtures.rawOriginAirport
import static pl.zycienakodach.esflights.modules.sharedkernel.domain.orderid.OrderIdTestFixtures.rawOrderId
import static pl.zycienakodach.esflights.sdk.ApplicationTestFixtures.inMemoryTestApplication

class CalculatingOrderTotalPriceProcessSpec extends Specification {

    def app = inMemoryTestApplication(new CalculatingOrderTotalPriceProcess())

    def "when flight order submitted then should calculate order total price"() {
        given:
        def orderId = rawOrderId()
        def customerId = rawCustomerId()
        def flightCourseId = rawFlightCourseId()
        def originAirport = rawOriginAirport()
        def destinationAirport = rawDestinationAirport()

        when:
        def event = new FlightOrderSubmitted(
                orderId,
                customerId,
                flightCourseId,
                originAirport,
                destinationAirport
        )
        var eventMetadata = app.eventOccurred(event)

        then:
        app.lastCommandCausedBy(eventMetadata.eventId()) == new CalculateOrderTotalPrice(orderId)
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
        app.lastCommandCausedBy(eventMetadata.eventId()) == new ApplyOrderPriceDiscount(orderId, 10)
    }

}

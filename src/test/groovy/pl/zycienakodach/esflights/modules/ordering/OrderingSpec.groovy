package pl.zycienakodach.esflights.modules.ordering

import pl.zycienakodach.esflights.modules.ordering.api.commands.OfferFlightCourseForSell
import pl.zycienakodach.esflights.modules.ordering.api.commands.SubmitFlightOrder
import pl.zycienakodach.esflights.modules.ordering.api.events.FlightCourseOfferedForSell
import pl.zycienakodach.esflights.modules.ordering.api.events.FlightOrderSubmitted
import spock.lang.Specification

import java.time.Instant
import java.time.temporal.ChronoUnit

import static pl.zycienakodach.esflights.modules.sharedkernel.domain.customerid.CustomerIdTestFixtures.rawCustomerId
import static pl.zycienakodach.esflights.modules.sharedkernel.domain.flightid.FlightCourseTestFixtures.rawFlightCourseId
import static pl.zycienakodach.esflights.modules.sharedkernel.domain.iata.IATAAirportsCodeFixtures.rawDestinationAirport
import static pl.zycienakodach.esflights.modules.sharedkernel.domain.iata.IATAAirportsCodeFixtures.rawOriginAirport
import static pl.zycienakodach.esflights.modules.sharedkernel.domain.orderid.OrderIdTestFixtures.rawOrderId
import static pl.zycienakodach.esflights.sdk.ApplicationTestFixtures.inMemoryTestApplication
import static pl.zycienakodach.esflights.sdk.application.EventStreamNameTestFixtures.testTenantEventStream
import static pl.zycienakodach.esflights.sdk.application.time.TimeProviderFixtures.isUtcMidnightOf
import static pl.zycienakodach.esflights.sdk.infrastructure.message.command.CommandResultTestFixtures.rejectionReason
import static pl.zycienakodach.esflights.sdk.infrastructure.message.command.CommandResultTestFixtures.wasRejected
import static pl.zycienakodach.esflights.sdk.infrastructure.message.command.CommandTestFixtures.aCommandMetadata

class OrderingSpec extends Specification {

    def timeProvider = isUtcMidnightOf(2021, 9, 2)
    def app = inMemoryTestApplication(new OrderingModule(timeProvider))

    def "offer flight for sell"() {
        given:
        final flightCourseId = rawFlightCourseId()
        final originAirport = rawOriginAirport()
        final destinationAirport = rawDestinationAirport()

        when:
        def offerFlightForSell = new OfferFlightCourseForSell(
                flightCourseId,
                originAirport,
                destinationAirport
        )
        def commandMetadata = aCommandMetadata()
        app.execute(offerFlightForSell, commandMetadata)

        then:
        app.lastEventCausedBy(commandMetadata.commandId()) == new FlightCourseOfferedForSell(
                flightCourseId,
                originAirport,
                destinationAirport
        )
    }

    def "submit flight order"() {
        given:
        def customerId = rawCustomerId()
        def flightCourseId = rawFlightCourseId()
        final originAirport = rawOriginAirport()
        final destinationAirport = rawDestinationAirport()

        and:
        def eventStream = testTenantEventStream("FlightCourseSells", flightCourseId)
        def flightCourseOfferedForSell = new FlightCourseOfferedForSell(
                flightCourseId,
                originAirport,
                destinationAirport
        )
        app.eventOccurred(eventStream, flightCourseOfferedForSell)

        when:
        def submitFlightOrder = new SubmitFlightOrder(
                customerId,
                flightCourseId
        )
        def commandMetadata = aCommandMetadata()
        app.execute(submitFlightOrder, commandMetadata)

        then:
        def orderId = rawOrderId(customerId, flightCourseId)
        app.lastEventCausedBy(commandMetadata.commandId()) == new FlightOrderSubmitted(
                orderId,
                customerId,
                flightCourseId,
                originAirport,
                destinationAirport
        )
    }

    def "submit order for past flight should fail"() {
        given:
        def customerId = rawCustomerId()
        def pastDate = twoDaysAgo()
        def flightCourseId = rawFlightCourseId(pastDate)

        and:
        flightOfferedForSell(flightCourseId)

        when:
        def submitFlightOrder = new SubmitFlightOrder(
                customerId,
                flightCourseId
        )
        def commandMetadata = aCommandMetadata()
        var result = app.execute(submitFlightOrder, commandMetadata)

        then:
        wasRejected(result)
        rejectionReason(result) == "The flight has already departure!"

        and:
        app.lastEventCausedBy(commandMetadata.commandId()) == null
    }

    def "submit already submitted order should fail"() {
        def customerId = rawCustomerId()
        def flightCourseId = rawFlightCourseId()
        given:
        def orderId = rawOrderId(customerId, flightCourseId)
        final originAirport = rawOriginAirport()
        final destinationAirport = rawDestinationAirport()

        and:
        def eventStream = testTenantEventStream("FlightCourseSells", flightCourseId)
        def flightOrderSubmitted = new FlightOrderSubmitted(
                orderId,
                customerId,
                flightCourseId,
                originAirport,
                destinationAirport
        )
        app.eventOccurred(eventStream, flightOrderSubmitted)

        when:
        def submitFlightOrder = new SubmitFlightOrder(
                customerId,
                flightCourseId
        )
        def commandMetadata = aCommandMetadata()
        var result = app.execute(submitFlightOrder, commandMetadata)

        then:
        wasRejected(result)
        rejectionReason(result) == "Order already submitted"

        and:
        app.lastEventCausedBy(commandMetadata.commandId()) == null
    }

    private def flightOfferedForSell(String flightCourseId) {
        final originAirport = rawOriginAirport()
        final destinationAirport = rawDestinationAirport()
        def flightCourseOfferedForSell = new FlightCourseOfferedForSell(
                flightCourseId,
                originAirport,
                destinationAirport
        )
        app.eventOccurred(testTenantEventStream("FlightCourseSells", flightCourseId), flightCourseOfferedForSell)
    }

    private Instant twoDaysAgo() {
        timeProvider.get().minus(2, ChronoUnit.DAYS)
    }
}

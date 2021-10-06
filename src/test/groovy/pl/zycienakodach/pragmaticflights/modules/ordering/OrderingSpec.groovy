package pl.zycienakodach.pragmaticflights.modules.ordering

import pl.zycienakodach.pragmaticflights.modules.ordering.api.commands.OfferFlightCourseForSell
import pl.zycienakodach.pragmaticflights.modules.ordering.api.commands.SubmitFlightOrder
import pl.zycienakodach.pragmaticflights.modules.ordering.api.events.FlightCourseOfferedForSell
import pl.zycienakodach.pragmaticflights.modules.ordering.api.events.FlightsOrderSubmitted
import pl.zycienakodach.pragmaticflights.sdk.infrastructure.message.event.InMemoryEventBus
import pl.zycienakodach.pragmaticflights.sdk.infrastructure.message.event.RecordingEventBus
import spock.lang.Specification

import java.time.LocalDate
import java.time.ZoneOffset

import static pl.zycienakodach.pragmaticflights.ApplicationTestFixtures.inMemoryApplication
import static pl.zycienakodach.pragmaticflights.ApplicationTestFixtures.test
import static pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.customerid.CustomerIdTestFixtures.rawCustomerId
import static pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.flightid.FlightCourseTestFixtures.rawFlightCourseId
import static pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.iata.IATAAirportsCodeFixtures.rawDestinationAirport
import static pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.iata.IATAAirportsCodeFixtures.rawOriginAirport
import static pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.orderid.OrderIdTestFixtures.rawOrderId
import static pl.zycienakodach.pragmaticflights.sdk.application.EventStreamNameTestFixtures.testTenantEventStream
import static pl.zycienakodach.pragmaticflights.sdk.application.time.TimeProviderFixtures.isUtcMidnightOf
import static pl.zycienakodach.pragmaticflights.sdk.infrastructure.message.command.CommandTestFixtures.aCommandMetadata

class OrderingSpec extends Specification {

    def eventBus = new RecordingEventBus(new InMemoryEventBus())
    def timeProvider = isUtcMidnightOf(2021, 9, 2)
    def app = test(inMemoryApplication(eventBus)
            .withModule(new OrderingModule(timeProvider)))

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
        eventBus.lastEventCausedBy(commandMetadata.commandId()) == new FlightCourseOfferedForSell(
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
        app.testEventOccurred(eventStream, flightCourseOfferedForSell)

        when:
        def offerFlightForSell = new SubmitFlightOrder(
                customerId,
                flightCourseId
        )
        def commandMetadata = aCommandMetadata()
        app.execute(offerFlightForSell, commandMetadata)

        then:
        def orderId = rawOrderId(customerId, flightCourseId)
        eventBus.lastEventCausedBy(commandMetadata.commandId()) == new FlightsOrderSubmitted(
                orderId,
                customerId,
                flightCourseId,
                originAirport,
                destinationAirport
        )
    }
}

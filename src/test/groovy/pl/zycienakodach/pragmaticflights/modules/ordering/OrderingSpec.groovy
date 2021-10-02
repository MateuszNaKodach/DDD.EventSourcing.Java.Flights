package pl.zycienakodach.pragmaticflights.modules.ordering

import pl.zycienakodach.pragmaticflights.modules.ordering.api.commands.OfferFlightCourseForSell
import pl.zycienakodach.pragmaticflights.modules.ordering.api.commands.SubmitFlightOrder
import pl.zycienakodach.pragmaticflights.modules.ordering.api.events.FlightCourseOfferedForSell
import pl.zycienakodach.pragmaticflights.modules.ordering.api.events.FlightsOrderSubmitted
import pl.zycienakodach.pragmaticflights.sdk.infrastructure.message.event.InMemoryEventBus
import pl.zycienakodach.pragmaticflights.sdk.infrastructure.message.event.RecordingEventBus
import spock.lang.Specification

import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneOffset

import static pl.zycienakodach.pragmaticflights.ApplicationTestFixtures.inMemoryApplication
import static pl.zycienakodach.pragmaticflights.sdk.application.time.TimeProviderFixtures.isUtcMidnightOf
import static pl.zycienakodach.pragmaticflights.sdk.application.time.TimeProviderFixtures.utc12h00mOf
import static pl.zycienakodach.pragmaticflights.sdk.infrastructure.message.command.CommandTestFixtures.aCommandMetadata

class OrderingSpec extends Specification {

    def "offer flight for sell"() {
        given:
        def eventBus = new RecordingEventBus(new InMemoryEventBus())
        def timeProvider = isUtcMidnightOf(2021, 9, 2)
        def app = inMemoryApplication(eventBus)
                .withModule(new OrderingModule(timeProvider))

        when:
        def offerFlightForSell = new OfferFlightCourseForSell(
                'ULA 00001 CDA',
                'FMA',
                'BVE',
                utc12h00mOf(2021, 10, 2)
        )
        def commandMetadata = aCommandMetadata()
        app.execute(offerFlightForSell, commandMetadata)

        then:
        eventBus.lastEventCausedBy(commandMetadata.commandId()) == new FlightCourseOfferedForSell(
                'ULA 00001 CDA',
                'FMA',
                'BVE',
                utc12h00mOf(2021, 10, 2)
        )
    }

    def "submit flight order"() {
        given:
        def eventBus = new RecordingEventBus(new InMemoryEventBus())
        def flightId = 'ULA 00001 CDA'
        def timeProvider = { LocalDate.of(2021, 10, 2).atStartOfDay().toInstant(ZoneOffset.UTC) }
        def app = inMemoryApplication(eventBus)
                .withModule(new OrderingModule(timeProvider))
        def orderId = 'orderId'
        def customerId = 'customerId'
        def flightDate = LocalDate.of(2021, 10, 2)

        when:
        def offerFlightForSell = new SubmitFlightOrder(
                orderId,
                customerId,
                flightId,
                flightDate
        )
        def commandMetadata = aCommandMetadata()
        app.execute(offerFlightForSell, commandMetadata)

        then:
        eventBus.lastEventCausedBy(commandMetadata.commandId()) == new FlightsOrderSubmitted(
                orderId,
                customerId,
                flightId,
                LocalTime.of(12, 0),
                flightDate,
                'FMA',
                'BVE'
        )
    }
}

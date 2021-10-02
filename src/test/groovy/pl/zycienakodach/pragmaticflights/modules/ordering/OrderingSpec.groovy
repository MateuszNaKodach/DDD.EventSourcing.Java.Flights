package pl.zycienakodach.pragmaticflights.modules.ordering

import pl.zycienakodach.pragmaticflights.modules.ordering.api.commands.OfferFlightForSell
import pl.zycienakodach.pragmaticflights.modules.ordering.api.commands.SubmitFlightOrder
import pl.zycienakodach.pragmaticflights.modules.ordering.api.events.FlightOfferedForSell
import pl.zycienakodach.pragmaticflights.modules.ordering.api.events.FlightsOrderSubmitted
import pl.zycienakodach.pragmaticflights.modules.ordering.application.FlightsOffers
import pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.flightid.FlightId
import pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.iata.IATAAirportCode
import pl.zycienakodach.pragmaticflights.readmodels.flightoffers.FlightOffer
import pl.zycienakodach.pragmaticflights.sdk.infrastructure.message.event.InMemoryEventBus
import pl.zycienakodach.pragmaticflights.sdk.infrastructure.message.event.RecordingEventBus
import spock.lang.Specification

import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneOffset

import static pl.zycienakodach.pragmaticflights.ApplicationTestFixtures.inMemoryApplication
import static pl.zycienakodach.pragmaticflights.sdk.infrastructure.message.command.CommandTestFixtures.aCommandMetadata

class OrderingSpec extends Specification {

    def "offer flight for sell"() {
        given:
        def eventBus = new RecordingEventBus(new InMemoryEventBus())
        def flightsOffers = Stub(FlightsOffers)
        def timeProvider = { Instant.now() }
        def app = inMemoryApplication(eventBus)
                .withModule(new OrderingModule(flightsOffers, timeProvider))

        when:
        def offerFlightForSell = new OfferFlightForSell(
                'ULA 00001 CDA',
                'FMA',
                'BVE',
                LocalTime.of(12, 0),
                Set.of(DayOfWeek.MONDAY)
        )
        def commandMetadata = aCommandMetadata()
        app.execute(offerFlightForSell, commandMetadata)

        then:
        eventBus.lastEventCausedBy(commandMetadata.commandId()) == new FlightOfferedForSell(
                'ULA 00001 CDA',
                'FMA',
                'BVE',
                LocalTime.of(12, 0),
                Set.of(DayOfWeek.MONDAY)
        )
    }

    def "submit flight order"() {
        given:
        def eventBus = new RecordingEventBus(new InMemoryEventBus())
        def flightId = 'ULA 00001 CDA'
        def flightsOffers = Stub(FlightsOffers) {
            findBy(FlightId.fromRaw(flightId)) >> Optional.of(new FlightOffer(
                    FlightId.fromRaw(flightId),
                    IATAAirportCode.fromRaw('FMA'),
                    IATAAirportCode.fromRaw('BVE'),
                    LocalTime.of(12, 0),
                    Set.of(DayOfWeek.SATURDAY)
            ))
        }
        def timeProvider = { LocalDate.of(2021, 10, 2).atStartOfDay().toInstant(ZoneOffset.UTC) }
        def app = inMemoryApplication(eventBus)
                .withModule(new OrderingModule(flightsOffers, timeProvider))
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

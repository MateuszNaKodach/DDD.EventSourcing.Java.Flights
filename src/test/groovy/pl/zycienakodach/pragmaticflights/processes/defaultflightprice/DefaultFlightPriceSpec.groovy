package pl.zycienakodach.pragmaticflights.processes.defaultflightprice

import pl.zycienakodach.pragmaticflights.modules.ordering.api.events.FlightCourseOfferedForSell
import pl.zycienakodach.pragmaticflights.modules.pricing.api.commands.DefineRegularPrice
import pl.zycienakodach.pragmaticflights.sdk.infrastructure.message.command.InMemoryCommandBus
import pl.zycienakodach.pragmaticflights.sdk.infrastructure.message.command.RecordingCommandBus
import spock.lang.Specification

import static pl.zycienakodach.pragmaticflights.ApplicationTestFixtures.inMemoryApplication
import static pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.flightid.FlightCourseTestFixtures.rawFlightCourseId
import static pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.iata.IATAAirportsCodeFixtures.rawDestinationAirport
import static pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.iata.IATAAirportsCodeFixtures.rawOriginAirport

class DefaultFlightPriceSpec extends Specification {

    def "when flight course offered for sell then should define regular price for the flight"() {
        given:
        var defaultPriceInEuro = 30.0
        var commandBus = new RecordingCommandBus(new InMemoryCommandBus());
        def app = inMemoryApplication(commandBus)
                .withModule(new DefaultFlightPriceProcess(defaultPriceInEuro))

        when:
        def flightCourseId = rawFlightCourseId()
        var eventMetadata = app.testEventOccurred(
                new FlightCourseOfferedForSell(
                        flightCourseId,
                        rawOriginAirport(),
                        rawDestinationAirport()
                )
        )

        then:
        commandBus.lastCommandCausedBy(eventMetadata.eventId()) == new DefineRegularPrice(flightCourseId, defaultPriceInEuro)
    }
}

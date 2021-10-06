package pl.zycienakodach.pragmaticflights.processes.defaultflightprice

import pl.zycienakodach.pragmaticflights.modules.ordering.api.events.FlightCourseOfferedForSell
import pl.zycienakodach.pragmaticflights.modules.pricing.api.commands.DefineRegularPrice
import spock.lang.Specification

import static pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.flightid.FlightCourseTestFixtures.rawFlightCourseId
import static pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.iata.IATAAirportsCodeFixtures.rawDestinationAirport
import static pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.iata.IATAAirportsCodeFixtures.rawOriginAirport
import static pl.zycienakodach.pragmaticflights.sdk.ApplicationTestFixtures.inMemoryTestApplication

class DefaultFlightPriceSpec extends Specification {

    def defaultPriceInEuro = 30.0
    def app = inMemoryTestApplication(new DefaultFlightPriceProcess(defaultPriceInEuro))

    def "when flight course offered for sell then should define regular price for the flight"() {
        when:
        def flightCourseId = rawFlightCourseId()
        var eventMetadata = app.eventOccurred(
                new FlightCourseOfferedForSell(
                        flightCourseId,
                        rawOriginAirport(),
                        rawDestinationAirport()
                )
        )

        then:
        app.lastCommandCausedBy(eventMetadata.eventId()) == new DefineRegularPrice(flightCourseId, defaultPriceInEuro)
    }
}

package pl.zycienakodach.esflights.processes.defaultflightprice

import pl.zycienakodach.esflights.modules.ordering.api.events.FlightCourseOfferedForSell
import pl.zycienakodach.esflights.modules.pricing.api.commands.DefineRegularPrice
import spock.lang.Specification

import static pl.zycienakodach.esflights.modules.sharedkernel.domain.flightid.FlightCourseTestFixtures.rawFlightCourseId
import static pl.zycienakodach.esflights.modules.sharedkernel.domain.iata.IATAAirportsCodeFixtures.rawDestinationAirport
import static pl.zycienakodach.esflights.modules.sharedkernel.domain.iata.IATAAirportsCodeFixtures.rawOriginAirport
import static pl.zycienakodach.esflights.sdk.ApplicationTestFixtures.inMemoryTestApplication

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

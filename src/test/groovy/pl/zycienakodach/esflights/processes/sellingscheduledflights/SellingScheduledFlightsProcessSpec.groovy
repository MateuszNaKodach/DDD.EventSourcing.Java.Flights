package pl.zycienakodach.esflights.processes.sellingscheduledflights

import pl.zycienakodach.esflights.modules.flightsschedule.api.events.FlightCourseScheduled
import pl.zycienakodach.esflights.modules.ordering.api.commands.OfferFlightCourseForSell
import spock.lang.Specification

import static pl.zycienakodach.esflights.modules.sharedkernel.domain.flightid.FlightCourseTestFixtures.rawFlightCourseId
import static pl.zycienakodach.esflights.modules.sharedkernel.domain.flightid.FlightIdTestFixtures.rawFlightId
import static pl.zycienakodach.esflights.modules.sharedkernel.domain.iata.IATAAirportsCodeFixtures.rawDestinationAirport
import static pl.zycienakodach.esflights.modules.sharedkernel.domain.iata.IATAAirportsCodeFixtures.rawOriginAirport
import static pl.zycienakodach.esflights.sdk.ApplicationTestFixtures.inMemoryTestApplication
import static pl.zycienakodach.esflights.sdk.application.time.TimeProviderFixtures.anyTime

class SellingScheduledFlightsProcessSpec extends Specification {

    def "when flight scheduled then should offer flight for sell"() {
        given:
        def app = inMemoryTestApplication(new SellingScheduledFlightsProcess())

        when:
        def flightId = rawFlightId()
        def departureAt = anyTime().get()
        def flightCourseId = rawFlightCourseId(flightId, departureAt)
        def originAirport = rawOriginAirport()
        def destinationAirport = rawDestinationAirport()

        def event = new FlightCourseScheduled(
                flightCourseId,
                flightId,
                originAirport,
                destinationAirport,
                departureAt
        )
        var eventMetadata = app.eventOccurred(event)

        then:
        app.lastCommandCausedBy(eventMetadata.eventId())
                == new OfferFlightCourseForSell(flightCourseId, originAirport, destinationAirport)
    }

}

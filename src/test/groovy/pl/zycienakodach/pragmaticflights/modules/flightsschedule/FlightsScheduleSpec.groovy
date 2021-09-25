package pl.zycienakodach.pragmaticflights.modules.flightsschedule

import pl.zycienakodach.pragmaticflights.flightsschedule.api.FlightScheduled
import pl.zycienakodach.pragmaticflights.flightsschedule.api.ScheduleFlight
import pl.zycienakodach.pragmaticflights.shared.application.message.command.CommandResult
import pl.zycienakodach.pragmaticflights.shared.infrastructure.message.event.InMemoryEventBus
import pl.zycienakodach.pragmaticflights.shared.infrastructure.message.event.RecordingEventBus
import pl.zycienakodach.pragmaticflights.sharedkernel.domain.flightid.FlightIdFactory
import pl.zycienakodach.pragmaticflights.sharedkernel.domain.iata.IATAAirlinesCodeFactory
import pl.zycienakodach.pragmaticflights.sharedkernel.domain.iata.IATAAirportCodeFactory
import spock.lang.Specification

import java.time.DayOfWeek
import java.time.LocalTime

import static pl.zycienakodach.pragmaticflights.ApplicationTestFixtures.inMemoryApplication
import static pl.zycienakodach.pragmaticflights.shared.infrastructure.message.command.CommandTestFixtures.aCommandMetadata
import static pl.zycienakodach.pragmaticflights.sharedkernel.domain.FlightIdTestFixtures.rawFlightId

class FlightsScheduleSpec extends Specification {

    def eventBus = new RecordingEventBus(new InMemoryEventBus())
    var iataAirlinesCodesFactory = new IATAAirlinesCodeFactory({ true })
    var iataAirportCodesFactory = new IATAAirportCodeFactory({ true })
    var flightIdFactory = new FlightIdFactory(iataAirlinesCodesFactory)
    def app = inMemoryApplication(eventBus)
            .withModule(new FlightsScheduleModule(flightIdFactory, iataAirportCodesFactory))
    def flightId = rawFlightId()
    def departureTime = LocalTime.of(2,45)

    def "should schedule new flight"() {
        when:
        def command = new ScheduleFlight(
                flightId,
                "BCA",
                "NYC",
                departureTime,
                Set.of(DayOfWeek.FRIDAY, DayOfWeek.SATURDAY, DayOfWeek.SUNDAY)
        )
        def metadata = aCommandMetadata()
        var result = app.execute(command, metadata)

        then:
        result instanceof CommandResult.Accepted

        and:
        eventBus.lastPublishedEvent() == new FlightScheduled(
                flightId,
                "BCA",
                "NYC",
                departureTime,
                Set.of(DayOfWeek.FRIDAY, DayOfWeek.SATURDAY, DayOfWeek.SUNDAY)
        )
    }

    def "should reject scheduling a flight without departure days"() {
        when:
        def command = new ScheduleFlight(
                flightId,
                "BCA",
                "NYC",
                departureTime,
                Set.of()
        )
        def metadata = aCommandMetadata()
        var result = app.execute(command, metadata)

        then:
        result instanceof CommandResult.Rejected
        result.failureReason().get() == "Flight must have at least one departure day."

        and:
        eventBus.lastPublishedEvent() == null
    }
}

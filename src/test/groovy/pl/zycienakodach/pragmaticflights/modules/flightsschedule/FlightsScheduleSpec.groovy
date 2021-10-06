package pl.zycienakodach.pragmaticflights.modules.flightsschedule

import pl.zycienakodach.pragmaticflights.modules.flightsschedule.api.commands.ScheduleFlightCourses
import pl.zycienakodach.pragmaticflights.modules.flightsschedule.api.events.FlightCourseScheduled
import pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.flightid.FlightIdFactory
import pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.iata.IATAAirlinesCodeFactory
import pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.iata.IATAAirportCodeFactory
import pl.zycienakodach.pragmaticflights.sdk.application.message.command.CommandResult
import spock.lang.Specification

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime

import static pl.zycienakodach.pragmaticflights.modules.flightsschedule.FlightScheduleTestFixtures.scheduledOnDates
import static pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.flightid.FlightIdTestFixtures.rawFlightId
import static pl.zycienakodach.pragmaticflights.sdk.ApplicationTestFixtures.inMemoryTestApplication
import static pl.zycienakodach.pragmaticflights.sdk.application.time.TimeProviderFixtures.isUtcMidnightOf
import static pl.zycienakodach.pragmaticflights.sdk.infrastructure.message.command.CommandTestFixtures.aCommandMetadata

class FlightsScheduleSpec extends Specification {

    def iataAirlinesCodesFactory = new IATAAirlinesCodeFactory({ true })
    def iataAirportCodesFactory = new IATAAirportCodeFactory({ true })
    def flightIdFactory = new FlightIdFactory(iataAirlinesCodesFactory)
    def timeProvider = isUtcMidnightOf(2020, 12, 31)
    def app = inMemoryTestApplication(new FlightsScheduleModule(timeProvider, flightIdFactory, iataAirportCodesFactory))
    def flightId = rawFlightId()
    def departureTime = LocalTime.of(2, 45)

    def "schedule flights on week days during selected period"() {
        when:
        def command = new ScheduleFlightCourses(
                flightId,
                "BCA",
                "NYC",
                departureTime,
                Set.of(DayOfWeek.FRIDAY, DayOfWeek.SUNDAY),
                LocalDate.of(2021, 1, 1),
                LocalDate.of(2021, 1, 31)
        )
        def metadata = aCommandMetadata()
        var result = app.execute(command, metadata)

        then:
        result instanceof CommandResult.Accepted

        and:
        def expectedDepartureDates = [
                "2021-01-01T02:45:00Z",
                "2021-01-03T02:45:00Z",
                "2021-01-08T02:45:00Z",
                "2021-01-10T02:45:00Z",
                "2021-01-15T02:45:00Z",
                "2021-01-17T02:45:00Z",
                "2021-01-22T02:45:00Z",
                "2021-01-24T02:45:00Z",
                "2021-01-29T02:45:00Z",
                "2021-01-31T02:45:00Z"
        ]
        app.eventsCausedBy(metadata.commandId()) == scheduledOnDates(expectedDepartureDates, {
            new FlightCourseScheduled(
                    flightId + " + " + it.toString(),
                    flightId,
                    "BCA",
                    "NYC",
                    it
            )
        })
    }

    def "should reject scheduling a flight without departure days"() {
        when:
        def command = new ScheduleFlightCourses(
                flightId,
                "BCA",
                "NYC",
                departureTime,
                Set.of(),
                LocalDate.of(2021, 1, 1),
                LocalDate.of(2021, 1, 31)
        )
        def metadata = aCommandMetadata()
        var result = app.execute(command, metadata)

        then:
        result instanceof CommandResult.Rejected
        result.failureReason().get() == "Flight must have at least one departure day."

        and:
        app.lastEventCausedBy(metadata.commandId()) == null
    }

    def "should reject scheduling a flight for the past"() {
        when:
        def command = new ScheduleFlightCourses(
                flightId,
                "BCA",
                "NYC",
                departureTime,
                Set.of(DayOfWeek.FRIDAY, DayOfWeek.SUNDAY),
                LocalDate.of(2010, 3, 1),
                LocalDate.of(2010, 5, 1)
        )
        def metadata = aCommandMetadata()
        var result = app.execute(command, metadata)

        then:
        result instanceof CommandResult.Rejected
        result.failureReason().get() == "The range must be in the future!"

        and:
        app.lastEventCausedBy(metadata.commandId()) == null
    }

    def "should reject scheduling a flight with invalid flightId"() {
        when:
        def command = new ScheduleFlightCourses(
                "NOT VALID",
                "BCA",
                "NYC",
                departureTime,
                Set.of(DayOfWeek.MONDAY),
                LocalDate.of(2021, 1, 1),
                LocalDate.of(2021, 1, 31)
        )
        def metadata = aCommandMetadata()
        var result = app.execute(command, metadata)

        then:
        result instanceof CommandResult.Rejected
        result.failureReason().get() == "Invalid flightId NOT VALID"

        and:
        app.lastEventCausedBy(metadata.commandId()) == null
    }
}

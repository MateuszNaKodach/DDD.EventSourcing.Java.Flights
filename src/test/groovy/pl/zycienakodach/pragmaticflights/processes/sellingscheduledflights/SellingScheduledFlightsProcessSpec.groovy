package pl.zycienakodach.pragmaticflights.processes.sellingscheduledflights

import pl.zycienakodach.pragmaticflights.modules.flightsschedule.api.events.FlightCourseScheduled
import pl.zycienakodach.pragmaticflights.modules.ordering.api.commands.OfferFlightCourseForSell
import pl.zycienakodach.pragmaticflights.sdk.application.EventStreamName
import pl.zycienakodach.pragmaticflights.sdk.infrastructure.message.command.InMemoryCommandBus
import pl.zycienakodach.pragmaticflights.sdk.infrastructure.message.command.RecordingCommandBus
import spock.lang.Specification

import java.time.DayOfWeek
import java.time.LocalTime

import static pl.zycienakodach.pragmaticflights.ApplicationTestFixtures.inMemoryApplication

class SellingScheduledFlightsProcessSpec extends Specification {

    def "when flight scheduled then should offer flight for sell"() {
        given:
        var commandBus = new RecordingCommandBus(new InMemoryCommandBus());
        def app = inMemoryApplication(commandBus)
                .withModule(new SellingScheduledFlightsProcess())

        when:
        var eventStream = new EventStreamName("category", "id")
        def flightId = "flightId"

        def event = new FlightCourseScheduled(
                flightId,
                "NYC",
                "NYC",
                LocalTime.now(),
                Set.of(DayOfWeek.MONDAY, DayOfWeek.SUNDAY)
        )
        var eventMetadata = app.eventOccurred(
                eventStream,
                event
        )

        then:
        commandBus.lastCommandCausedBy(eventMetadata.eventId())
                == new OfferFlightCourseForSell(flightId, "NYC", "NYC", event.departureTime(), event.departureDays())
    }

}

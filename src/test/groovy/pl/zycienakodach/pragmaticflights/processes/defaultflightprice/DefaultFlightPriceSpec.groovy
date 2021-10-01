package pl.zycienakodach.pragmaticflights.processes.defaultflightprice

import pl.zycienakodach.pragmaticflights.modules.ordering.api.events.FlightOfferedForSell
import pl.zycienakodach.pragmaticflights.modules.pricing.api.commands.DefineRegularPrice
import pl.zycienakodach.pragmaticflights.sdk.application.EventStreamName
import pl.zycienakodach.pragmaticflights.sdk.infrastructure.message.command.InMemoryCommandBus
import pl.zycienakodach.pragmaticflights.sdk.infrastructure.message.command.RecordingCommandBus
import spock.lang.Specification

import java.time.DayOfWeek
import java.time.LocalTime

import static pl.zycienakodach.pragmaticflights.ApplicationTestFixtures.inMemoryApplication

class DefaultFlightPriceSpec extends Specification {

    def "when flight offered for sell then should define regular price for each day"() {
        given:
        var defaultPriceInEuro = 30.0
        var commandBus = new RecordingCommandBus(new InMemoryCommandBus());
        def app = inMemoryApplication(commandBus)
                .withModule(new DefaultFlightPriceProcess(defaultPriceInEuro))

        when:
        var eventStream = new EventStreamName("category", "id")
        def flightId = "flightId"
        var eventMetadata = app.eventOccurred(
                eventStream,
                new FlightOfferedForSell(
                        flightId,
                        "NYC",
                        "NYC",
                        LocalTime.now(),
                        Set.of(DayOfWeek.MONDAY, DayOfWeek.SUNDAY)
                )
        )

        then:
        commandBus.commandsCausedBy(eventMetadata.eventId())
                .containsAll([
                        new DefineRegularPrice(flightId, DayOfWeek.MONDAY, defaultPriceInEuro),
                        new DefineRegularPrice(flightId, DayOfWeek.SUNDAY, defaultPriceInEuro)
                ])

    }
}

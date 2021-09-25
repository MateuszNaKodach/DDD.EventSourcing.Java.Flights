package pl.zycienakodach.pragmaticflights.pricing

import pl.zycienakodach.pragmaticflights.pricing.api.DefineFlightRegularPrice
import pl.zycienakodach.pragmaticflights.shared.infrastructure.message.event.InMemoryEventBus
import spock.lang.Specification

import java.time.DayOfWeek

import static pl.zycienakodach.pragmaticflights.ApplicationTestFixtures.inMemoryApplication
import static pl.zycienakodach.pragmaticflights.shared.infrastructure.message.command.CommandTestFixtures.aCommandMetadata

class PricingSpec extends Specification {

    def "Name"() {
        given:
        def eventBus = new InMemoryEventBus()
        def app = inMemoryApplication(eventBus).withModule(new PricingModule())

        and:
        def flightId = "KLM 12345 BCA";
        def command = new DefineFlightRegularPrice(
                flightId,
                DayOfWeek.MONDAY,
                1400
        )
        def metadata = aCommandMetadata()

        when:
        app.execute(command, metadata)

        then:
        1 * eventBus.publish(_)
    }
}

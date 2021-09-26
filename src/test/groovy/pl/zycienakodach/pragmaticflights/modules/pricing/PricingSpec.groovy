package pl.zycienakodach.pragmaticflights.modules.pricing

import pl.zycienakodach.pragmaticflights.modules.pricing.api.DefineRegularPrice
import pl.zycienakodach.pragmaticflights.sdk.infrastructure.message.event.InMemoryEventBus
import pl.zycienakodach.pragmaticflights.sdk.infrastructure.message.event.RecordingEventBus
import spock.lang.Specification

import java.time.DayOfWeek

import static pl.zycienakodach.pragmaticflights.ApplicationTestFixtures.inMemoryApplication
import static pl.zycienakodach.pragmaticflights.sdk.infrastructure.message.command.CommandTestFixtures.aCommandMetadata

class PricingSpec extends Specification {

    def "Name"() {
        given:
        def eventBus = new RecordingEventBus(new InMemoryEventBus())
        def app = inMemoryApplication(eventBus).withModule(new PricingModule())

        and:
        def flightId = "KLM 12345 BCA";
        def command = new DefineRegularPrice(
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
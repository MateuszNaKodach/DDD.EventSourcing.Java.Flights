package pl.zycienakodach.pragmaticflights.pricing

import pl.zycienakodach.pragmaticflights.pricing.api.DefineFlightPrice
import spock.lang.Shared
import spock.lang.Specification

import java.time.DayOfWeek

import static pl.zycienakodach.pragmaticflights.ApplicationTestFixtures.inMemoryApplication

class PricingSpec extends Specification {

    @Shared
    def app = inMemoryApplication().withModule(new PricingModule())

    def "Name"() {
        given:
        def flightId = "KLM 12345 BCA";
        def command = new DefineFlightPrice(
                flightId,
                DayOfWeek.MONDAY,
                1400
        )
        app.execute(command)
    }
}

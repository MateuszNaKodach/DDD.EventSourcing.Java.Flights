package pl.zycienakodach.pragmaticflights.pricing

import pl.zycienakodach.pragmaticflights.pricing.api.DefineFlightPrice
import spock.lang.Specification

import static pl.zycienakodach.pragmaticflights.ApplicationTestFixtures.inMemoryApplication

class PricingSpec extends Specification {

    def "Name"() {
        def sut = inMemoryApplication().withModule(new PricingModule())

        //sut.execute(new DefineFlightPrice())
    }
}

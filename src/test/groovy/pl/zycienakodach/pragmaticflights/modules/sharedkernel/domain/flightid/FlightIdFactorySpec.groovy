package pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.flightid

import pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.iata.IATAAirlinesCodeFactory
import spock.lang.Specification

class FlightIdFactorySpec extends Specification {

    def "should not create id if airlines code is not valid IATA code"() {
        given:
        def airlinesCodeIsInvalid = new IATAAirlinesCodeFactory({ false })
        def flightIdFactory = new FlightIdFactory(airlinesCodeIsInvalid)

        when:
        flightIdFactory.flightId("ABC 12345 DEF")

        then:
        def thrownException = thrown(RuntimeException)
        thrownException.message == "ABC is not valid IATA Airlines code!"
    }

    def "should not create id if is not well formatted"() {
        given:
        def airlinesCodeIsInvalid = new IATAAirlinesCodeFactory({ true })
        def flightIdFactory = new FlightIdFactory(airlinesCodeIsInvalid)

        when:
        flightIdFactory.flightId("ABC notWell formatted")

        then:
        def thrownException = thrown(RuntimeException)
        thrownException.message == "Invalid flightId ABC notWell formatted"
    }

    def "should create id if airlines code is valid IATA code"() {
        given:
        def airlinesCodeIsInvalid = new IATAAirlinesCodeFactory({ true })
        def flightIdFactory = new FlightIdFactory(airlinesCodeIsInvalid)

        when:
        var result = flightIdFactory.flightId("ABC 12345 DEF")

        then:
        result.raw() == "ABC 12345 DEF"
    }

}

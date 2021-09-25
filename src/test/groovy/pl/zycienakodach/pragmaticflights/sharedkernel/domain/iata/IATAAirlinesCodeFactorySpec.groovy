package pl.zycienakodach.pragmaticflights.sharedkernel.domain.iata

import spock.lang.Specification

class IATAAirlinesCodeFactorySpec extends Specification {

    def "should reject invalid IATA code"() {
        given:
        def factory = new IATAAirlinesCodeFactory({ false })
        def rawCode = "InvalidCode"

        when:
        def code = factory.code(rawCode)

        then:
        def thrownException = thrown(RuntimeException)
        thrownException.message == "InvalidCode is not valid IATA Airlines code!"
    }

    def "should accept valid IATA code"() {
        given:
        def factory = new IATAAirlinesCodeFactory({ true })
        def rawCode = "UA"

        when:
        def code = factory.code(rawCode)

        then:
        code.raw() == "UA"
    }
}

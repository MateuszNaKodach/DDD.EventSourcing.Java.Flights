package pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.flightid

import spock.lang.Specification

class FlightCourseIdSpec extends Specification {

    def "Of"() {
        when:
        def flightId = FlightId.fromRaw("KLM 12345 ABC")
        val departureAt =
        def flightCourseId = FlightCourseId.of(flightId, )

    }

    def "FromRaw"() {
    }

    def "Raw"() {
    }

    def "FlightId"() {
    }

    def "DepartureAt"() {
    }
}

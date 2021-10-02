package pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.flightid

import spock.lang.Specification

import static pl.zycienakodach.pragmaticflights.sdk.application.time.TimeProviderFixtures.utc12h00mOf

class FlightCourseIdSpec extends Specification {

    def "is composed from flightId and departure date"() {
        given:
        def flightId = FlightId.fromRaw("KLM 12345 ABC")
        def departureAt = utc12h00mOf(2012,12,12)

        when:
        def flightCourseId = FlightCourseId.of(flightId, departureAt)

        then:
        flightCourseId.raw() == 'KLM 12345 ABC + 2012-12-12T12:00:00Z'
    }

    def "can be parsed from raw string"() {
        given:
        def raw = 'KLM 12345 ABC + 2012-12-12T12:00:00Z'

        when:
        def flightCourseId = FlightCourseId.fromRaw(raw)

        then:
        flightCourseId.raw() == 'KLM 12345 ABC + 2012-12-12T12:00:00Z'
        flightCourseId.flightId() == FlightId.fromRaw("KLM 12345 ABC")
        flightCourseId.departureAt() == utc12h00mOf(2012,12,12)
    }

}

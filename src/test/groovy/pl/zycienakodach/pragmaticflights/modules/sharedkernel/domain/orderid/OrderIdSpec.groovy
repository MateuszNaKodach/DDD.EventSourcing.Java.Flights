package pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.orderid

import spock.lang.Specification

import static pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.customerid.CustomerIdTestFixtures.aCustomerId
import static pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.flightid.FlightCourseTestFixtures.aFlightCourseId
import static pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.flightid.FlightCourseTestFixtures.flightDepartureDate

class OrderIdSpec extends Specification {

    def "is composed from CustomerId and FlightCourseId"() {
        given:
        final customerId = aCustomerId()
        final flightDepartureDate = flightDepartureDate()
        final flightCourseId = aFlightCourseId(flightDepartureDate)

        when:
        final orderId = OrderId.of(customerId, flightCourseId)

        then:
        orderId.raw() == customerId.raw() + ' & ' + flightCourseId.raw()
    }

    def "can be parsed from raw string"() {
        given:
        final raw = 'ad562e7c-aa26-4017-b446-28a0f9da7e2c & KLM 12345 ABC + 2021-10-03T00:39:19.726150Z'

        when:
        final orderId = OrderId.fromRaw(raw)

        then:
        orderId.customerId().raw() == 'ad562e7c-aa26-4017-b446-28a0f9da7e2c'
        orderId.flightCourseId().raw() == 'KLM 12345 ABC + 2021-10-03T00:39:19.726150Z'
    }
}

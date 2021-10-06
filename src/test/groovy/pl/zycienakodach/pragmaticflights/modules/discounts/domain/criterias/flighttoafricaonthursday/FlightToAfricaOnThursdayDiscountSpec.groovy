package pl.zycienakodach.pragmaticflights.modules.discounts.domain.criterias.flighttoafricaonthursday

import pl.zycienakodach.pragmaticflights.modules.discounts.domain.criterias.DiscountCriteriaName
import pl.zycienakodach.pragmaticflights.modules.discounts.domain.criterias.FlightOrder
import pl.zycienakodach.pragmaticflights.modules.discounts.domain.criterias.Orders
import pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.flightid.FlightId
import pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.iata.IATAAirportCode
import pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.orderid.OrderId
import spock.lang.Specification

import java.time.LocalDate
import java.time.LocalTime

import static pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.customerid.CustomerIdTestFixtures.aCustomerId
import static pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.iata.IATAAirportsCodeFixtures.aDestinationAirport
import static pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.iata.IATAAirportsCodeFixtures.anOriginAirport
import static pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.orderid.OrderIdTestFixtures.anOrderId

class FlightToAfricaOnThursdayDiscountSpec extends Specification {

    def "when flight is on thursday and destination airport is in Africa then discount should be 5 EURO"(
            boolean onThursday,
            boolean toAfrica,
            BigDecimal expectedDiscount,
            List<DiscountCriteriaName> expectedAppliedCriteria
    ) {
        given:
        def orderId = anOrderId()
        def destination = aDestinationAirport()
        def flightDate = onThursday ? flightOnThursday() : flightNotOnThursday()

        and:
        def airportsContinents = airportIsOnContinent(destination, toAfrica ? Continent.AFRICA : Continent.EUROPE)
        def orders = orderedFlight(orderId, destination, flightDate)

        when:
        def criteria = new FlightToAfricaOnThursdayDiscount(orders, airportsContinents)
        def discount = criteria.calculateDiscount(orderId)

        then:
        discount.euro().toBigDecimal() == expectedDiscount

        and:
        discount.appliedCriteria() == expectedAppliedCriteria

        where:
        onThursday | toAfrica || expectedDiscount | expectedAppliedCriteria
        true       | true     || 5                | [DiscountCriteriaName.of("FlightToAfricaOnThursdayDiscount")]
        false      | true     || 0                | []
        true       | false    || 0                | []
        false      | false    || 0                | []
    }

    def "when continent for airport is not known, then do not apply discount"() {
        given:
        def orderId = anOrderId()
        def destination = aDestinationAirport()

        and:
        def airportsContinents = unknownAirportContinent(destination)
        def orders = orderedFlight(orderId, destination, flightOnThursday())

        when:
        def criteria = new FlightToAfricaOnThursdayDiscount(orders, airportsContinents)
        def discount = criteria.calculateDiscount(orderId)

        then:
        discount.euro().toBigDecimal() == BigDecimal.ZERO

        and:
        discount.appliedCriteria().isEmpty()
    }

    private AirportsContinents airportIsOnContinent(IATAAirportCode airport, Continent continent) {
        Stub(AirportsContinents) {
            continentOf(airport) >> Optional.of(continent)
        }
    }

    private AirportsContinents unknownAirportContinent(IATAAirportCode airport) {
        Stub(AirportsContinents) {
            continentOf(airport) >> Optional.empty()
        }
    }

    private Orders orderedFlight(OrderId orderId, IATAAirportCode destination, LocalDate flightDate) {
        def customerId = aCustomerId()
        def flightOrder = new FlightOrder(
                orderId,
                customerId,
                flightDate,
                new FlightOrder.Flight(
                        FlightId.fromRaw("UAL 22333 NBO"),
                        anOriginAirport(),
                        destination,
                        LocalTime.of(19, 45)
                )
        )
        return Stub(Orders) {
            findByOrderId(orderId) >> Optional.of(flightOrder)
        }
    }

    private static LocalDate flightOnThursday() {
        return LocalDate.of(2021, 9, 30)
    }

    private static LocalDate flightNotOnThursday() {
        return LocalDate.of(2021, 9, 29)
    }
}

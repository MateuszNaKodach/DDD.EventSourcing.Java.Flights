package pl.zycienakodach.pragmaticflights.modules.discounts.domain.criterias.flightdepartureoncustomerbirthday

import pl.zycienakodach.pragmaticflights.modules.discounts.domain.criterias.DiscountCriteriaName
import pl.zycienakodach.pragmaticflights.modules.discounts.domain.criterias.FlightOrder
import pl.zycienakodach.pragmaticflights.modules.discounts.domain.criterias.Orders
import pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.customerid.CustomerId
import pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.flightid.FlightId
import pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.orderid.OrderId
import spock.lang.Specification

import java.time.LocalDate
import java.time.LocalTime

import static pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.customerid.CustomerIdTestFixtures.aCustomerId
import static pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.iata.IATAAirportsCodeFixtures.aDestinationAirport
import static pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.iata.IATAAirportsCodeFixtures.anOriginAirport
import static pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.orderid.OrderIdTestFixtures.anOrderId

class FlightDepartureOnCustomerBirthdayDiscountSpec extends Specification {

    def "when a customer has birthday on departure date then discount should be 5 EURO"(
            LocalDate customerBirthday,
            LocalDate flightDate,
            BigDecimal expectedDiscount,
            List<DiscountCriteriaName> expectedAppliedCriteria
    ) {
        given:
        def orderId = anOrderId()
        def customerId = aCustomerId()

        and:
        def orders = orderedFlight(orderId, customerId, flightDate)
        def birthdays = customerHaveBirthdayOn(customerId, customerBirthday)

        when:
        def criteria = new FlightDepartureOnCustomerBirthdayDiscount(orders, birthdays)
        def discount = criteria.calculateDiscount(orderId)

        then:
        discount.euro().toBigDecimal() == expectedDiscount

        and:
        discount.appliedCriteria() == expectedAppliedCriteria

        where:
        customerBirthday           | flightDate                 || expectedDiscount | expectedAppliedCriteria
        LocalDate.of(1996, 8, 23)  | LocalDate.of(2021, 8, 23)  || 5                | [DiscountCriteriaName.of("FlightDepartureOnCustomerBirthdayDiscount")]
        LocalDate.of(2010, 8, 23)  | LocalDate.of(2021, 8, 23)  || 5                | [DiscountCriteriaName.of("FlightDepartureOnCustomerBirthdayDiscount")]
        LocalDate.of(1920, 10, 10) | LocalDate.of(2021, 10, 10) || 5                | [DiscountCriteriaName.of("FlightDepartureOnCustomerBirthdayDiscount")]
        LocalDate.of(2020, 12, 12) | LocalDate.of(2021, 1, 2)   || 0                | []
    }

    private Orders orderedFlight(OrderId orderId, CustomerId customerId, LocalDate flightDate) {
        def flightOrder = new FlightOrder(
                orderId,
                customerId,
                flightDate,
                new FlightOrder.Flight(
                        FlightId.fromRaw("UAL 22333 NBO"),
                        anOriginAirport(),
                        aDestinationAirport(),
                        LocalTime.of(19, 45)
                )
        )
        return Stub(Orders) {
            findByOrderId(orderId) >> Optional.of(flightOrder)
        }
    }

    private CustomersBirthdays customerHaveBirthdayOn(CustomerId customerId, LocalDate birthday) {
        Stub(CustomersBirthdays) {
            forCustomer(customerId) >> Optional.of(birthday)
        }
    }

}

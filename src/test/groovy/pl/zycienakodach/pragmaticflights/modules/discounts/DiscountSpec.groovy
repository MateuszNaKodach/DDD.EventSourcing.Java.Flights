package pl.zycienakodach.pragmaticflights.modules.discounts

import pl.zycienakodach.pragmaticflights.modules.discounts.api.command.CalculateDiscountValue
import pl.zycienakodach.pragmaticflights.modules.discounts.api.event.DiscountValueCalculated
import pl.zycienakodach.pragmaticflights.modules.discounts.domain.criterias.FlightOrder
import pl.zycienakodach.pragmaticflights.modules.discounts.domain.criterias.Orders
import pl.zycienakodach.pragmaticflights.modules.discounts.domain.criterias.flightdepartureoncustomerbirthday.CustomersBirthdays
import pl.zycienakodach.pragmaticflights.modules.discounts.domain.criterias.flighttoafricaonthursday.AirportsContinents
import pl.zycienakodach.pragmaticflights.modules.discounts.domain.criterias.flighttoafricaonthursday.Continent
import pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.customerid.CustomerId
import pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.flightid.FlightId
import pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.orderid.OrderId
import pl.zycienakodach.pragmaticflights.sdk.infrastructure.message.event.InMemoryEventBus
import pl.zycienakodach.pragmaticflights.sdk.infrastructure.message.event.RecordingEventBus
import spock.lang.Specification

import java.time.LocalDate
import java.time.LocalTime

import static pl.zycienakodach.pragmaticflights.ApplicationTestFixtures.inMemoryApplication
import static pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.iata.IATAAirportsCodeFixtures.jomoKenyattaInternationalAirportNairobiKenyaAfrica
import static pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.iata.IATAAirportsCodeFixtures.londonCityAirportLondonEnglandEurope
import static pl.zycienakodach.pragmaticflights.sdk.infrastructure.message.command.CommandTestFixtures.aCommandMetadata

class DiscountSpec extends Specification {

    def "apply two discounts for regular price of 30 EURO"() {
        given: 'customer ordered flight from Europe to Africa on Thursday'
        def customerId = new CustomerId("customerId")
        def orderId = new OrderId("orderId")

        def flightDate = LocalDate.of(2021, 9, 30)

        def destination = jomoKenyattaInternationalAirportNairobiKenyaAfrica()
        def airportsContinents = airportIsOnContinent(destination, Continent.AFRICA)
        def flightOrder = new FlightOrder(
                orderId,
                customerId,
                flightDate,
                new FlightOrder.Flight(
                        FlightId.fromRaw("UAL 22333 NBO"),
                        londonCityAirportLondonEnglandEurope(),
                        destination,
                        LocalTime.of(19, 45)
                )
        )

        def flightsOrders = Stub(Orders) {
            findByOrderId(orderId) >> Optional.of(flightOrder)
        }

        and: 'customer has birthday on flight date'
        def customersBirthdays = customerHaveBirthdayOn(customerId, flightDate)

        and: 'discounting is ready'
        def eventBus = new RecordingEventBus(new InMemoryEventBus())
        def app = inMemoryApplication(eventBus)
                .withModule(new DiscountsModule(flightsOrders, airportsContinents, customersBirthdays))

        when: 'calculate discount for given order with regular price 30 EURO'
        def euro30CommandMetadata = aCommandMetadata()
        app.execute(new CalculateDiscountValue(orderId.raw(), 30), euro30CommandMetadata)

        then: 'discount should be 10 EURO'
        eventBus.lastEventCausedBy(euro30CommandMetadata.commandId()) == new DiscountValueCalculated(orderId.raw(), 10)

        when: 'calculate discount for given order with regular price 21 EURO'
        def euro21CommandMetadata = aCommandMetadata()
        app.execute(new CalculateDiscountValue(orderId.raw(), 21), euro21CommandMetadata)

        then: 'discount should be 0 EURO'
        eventBus.lastEventCausedBy(euro21CommandMetadata.commandId()) == new DiscountValueCalculated(orderId.raw(), 0)
    }

    private AirportsContinents airportIsOnContinent(destination, continent) {
        Stub(AirportsContinents) {
            continentOf(destination) >> Optional.of(continent)
        }
    }

    private CustomersBirthdays customerHaveBirthdayOn(customerId, flightDate) {
        Stub(CustomersBirthdays) {
            forCustomer(customerId) >> Optional.of(flightDate)
        }
    }
}

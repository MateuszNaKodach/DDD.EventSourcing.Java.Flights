package pl.zycienakodach.pragmaticflights.modules.discounts

import pl.zycienakodach.pragmaticflights.modules.discounts.api.command.CalculateDiscountValue
import pl.zycienakodach.pragmaticflights.modules.discounts.api.event.DiscountValueCalculated
import pl.zycienakodach.pragmaticflights.modules.discounts.application.AppliedDiscountsRegistry
import pl.zycienakodach.pragmaticflights.modules.discounts.domain.criterias.DiscountCriteriaName
import pl.zycienakodach.pragmaticflights.modules.discounts.domain.criterias.FlightOrder
import pl.zycienakodach.pragmaticflights.modules.discounts.domain.criterias.Orders
import pl.zycienakodach.pragmaticflights.modules.discounts.domain.criterias.flightdepartureoncustomerbirthday.CustomersBirthdays
import pl.zycienakodach.pragmaticflights.modules.discounts.domain.criterias.flighttoafricaonthursday.AirportsContinents
import pl.zycienakodach.pragmaticflights.modules.discounts.domain.criterias.flighttoafricaonthursday.Continent
import pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.customerid.CustomerId
import pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.flightid.FlightId
import pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.orderid.OrderId
import pl.zycienakodach.pragmaticflights.sdk.application.tenant.TenantGroupId
import pl.zycienakodach.pragmaticflights.sdk.application.tenant.TenantGroups
import pl.zycienakodach.pragmaticflights.sdk.application.tenant.TenantId
import pl.zycienakodach.pragmaticflights.sdk.infrastructure.message.event.InMemoryEventBus
import pl.zycienakodach.pragmaticflights.sdk.infrastructure.message.event.RecordingEventBus
import spock.lang.Specification
import spock.lang.Unroll

import java.time.LocalDate
import java.time.LocalTime

import static pl.zycienakodach.pragmaticflights.ApplicationTestFixtures.inMemoryApplication
import static pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.iata.IATAAirportsCodeFixtures.jomoKenyattaInternationalAirportNairobiKenyaAfrica
import static pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.iata.IATAAirportsCodeFixtures.londonCityAirportLondonEnglandEurope
import static pl.zycienakodach.pragmaticflights.sdk.infrastructure.message.command.CommandTestFixtures.aCommandMetadata

class DiscountsSpec extends Specification {

    @Unroll
    def "when apply at most two discounts for regular price of #regularPrice EURO, then discount should be #expectedDiscount EURO"(double regularPrice, String tenantGroup, double expectedDiscount, boolean shouldSaveAppliedDiscountCriteria) {
        given: 'customer ordered flight'
        def customerId = new CustomerId("customerId")

        and: 'flight departures on Thursday'
        def flightDate = LocalDate.of(2021, 9, 30)

        and: 'flight destination airport in in Africa'
        def destination = jomoKenyattaInternationalAirportNairobiKenyaAfrica()
        def airportsContinents = airportIsOnContinent(destination, Continent.AFRICA)
        def orderId = new OrderId("orderId")
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
        def tenant = new TenantId("tenant1")
        def tenantGroups = Stub(TenantGroups) {
            tenantGroupOf(tenant) >> new TenantGroupId(tenantGroup)
        }
        def appliedDiscountsRegistry = Mock(AppliedDiscountsRegistry)
        def app = inMemoryApplication(eventBus)
                .withModule(new DiscountsModule(tenantGroups, appliedDiscountsRegistry, flightsOrders, airportsContinents, customersBirthdays))

        when: 'calculate discount for given order'
        def commandMetadata = aCommandMetadata(tenant)
        app.execute(new CalculateDiscountValue(orderId.raw(), regularPrice), commandMetadata)

        then: 'discount should be #expectedDiscount EURO'
        eventBus.lastEventCausedBy(commandMetadata.commandId()) == new DiscountValueCalculated(orderId.raw(), expectedDiscount)

        and: 'applied discounts'
        if (shouldSaveAppliedDiscountCriteria) {
            1 * appliedDiscountsRegistry.save(orderId, _)
            // List.of(new DiscountCriteriaName('FlightDepartureOnCustomerBirthdayDiscount'), new DiscountCriteriaName('FlightToAfricaOnThursdayDiscount'))
        } else {
            0 * appliedDiscountsRegistry.save(_, _)
        }

        where:
        regularPrice | tenantGroup | expectedDiscount | shouldSaveAppliedDiscountCriteria
        30           | "A"         | 10               | true
        21           | "A"         | 0                | true
        25           | "A"         | 5                | true
        30           | "B"         | 10               | false
        21           | "B"         | 0                | false
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

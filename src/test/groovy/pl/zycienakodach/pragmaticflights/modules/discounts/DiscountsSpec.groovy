package pl.zycienakodach.pragmaticflights.modules.discounts

import pl.zycienakodach.pragmaticflights.modules.discounts.api.command.CalculateDiscountValue
import pl.zycienakodach.pragmaticflights.modules.discounts.api.event.DiscountValueCalculated
import pl.zycienakodach.pragmaticflights.modules.discounts.application.AppliedDiscountsRegistry
import pl.zycienakodach.pragmaticflights.modules.discounts.domain.criterias.flightdepartureoncustomerbirthday.CustomersBirthdays
import pl.zycienakodach.pragmaticflights.modules.discounts.domain.criterias.flighttoafricaonthursday.AirportsContinents
import pl.zycienakodach.pragmaticflights.modules.discounts.domain.criterias.flighttoafricaonthursday.Continent
import pl.zycienakodach.pragmaticflights.modules.discounts.infrastructure.flightorders.InMemoryFlightOrders
import pl.zycienakodach.pragmaticflights.modules.ordering.api.events.FlightOrderSubmitted
import pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.customerid.CustomerId
import pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.iata.IATAAirportCode
import pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.orderid.OrderId
import pl.zycienakodach.pragmaticflights.sdk.TestApplication
import pl.zycienakodach.pragmaticflights.sdk.application.tenant.TenantGroupId
import pl.zycienakodach.pragmaticflights.sdk.application.tenant.TenantGroups
import pl.zycienakodach.pragmaticflights.sdk.application.tenant.TenantId
import spock.lang.Specification
import spock.lang.Unroll

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset

import static pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.customerid.CustomerIdTestFixtures.rawCustomerId
import static pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.flightid.FlightCourseTestFixtures.rawFlightCourseId
import static pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.iata.IATAAirportsCodeFixtures.jomoKenyattaInternationalAirportNairobiKenyaAfrica
import static pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.iata.IATAAirportsCodeFixtures.rawOriginAirport
import static pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.orderid.OrderIdTestFixtures.rawOrderId
import static pl.zycienakodach.pragmaticflights.sdk.ApplicationTestFixtures.inMemoryTestApplication
import static pl.zycienakodach.pragmaticflights.sdk.infrastructure.message.command.CommandTestFixtures.aCommandMetadata

// todo: make this test more generic
class DiscountsSpec extends Specification {

    @Unroll
    def "when apply at most two discounts for regular price of #regularPrice EURO, then discount should be #expectedDiscount EURO"(
            BigDecimal regularPrice,
            String tenantGroup,
            BigDecimal expectedDiscount,
            boolean shouldSaveAppliedDiscountCriteria
    ) {
        given: 'customer ordered flight'
        def customerId = rawCustomerId()

        and: 'flight departures on Thursday'
        def flightDate = LocalDate.of(2021, 9, 30)

        and: 'flight destination airport in in Africa'
        def destination = jomoKenyattaInternationalAirportNairobiKenyaAfrica()
        def airportsContinents = airportIsOnContinent(destination, Continent.AFRICA)

        and: 'customer has birthday on flight date'
        def customersBirthdays = customerHaveBirthdayOn(customerId, flightDate)

        and: 'tenant is in group #tenntGroup'
        def tenant = new TenantId("tenant1")
        def tenantGroups = Stub(TenantGroups) {
            tenantGroupOf(tenant) >> new TenantGroupId(tenantGroup)
        }
        def appliedDiscountsRegistry = Mock(AppliedDiscountsRegistry)
        def app = discounts(tenantGroups, appliedDiscountsRegistry, airportsContinents, customersBirthdays)

        and: 'flight order submitted'
        String orderId = flightOrderSubmitted(customerId, destination, flightDate, app)

        when: 'calculate discount for given order'
        def commandMetadata = aCommandMetadata(tenant)
        app.execute(new CalculateDiscountValue(orderId, regularPrice), commandMetadata)

        then: 'discount should be #expectedDiscount EURO'
        app.lastEventCausedBy(commandMetadata.commandId()) == new DiscountValueCalculated(orderId, expectedDiscount)

        and: 'applied discounts'
        if (shouldSaveAppliedDiscountCriteria) {
            1 * appliedDiscountsRegistry.save(OrderId.fromRaw(orderId), _)
        } else {
            0 * appliedDiscountsRegistry.save(_, _)
        }

        where:
        regularPrice | tenantGroup | expectedDiscount | shouldSaveAppliedDiscountCriteria
        30.0g        | "A"         | 10g              | true
        21.0g        | "A"         | 0g               | true
        25.0g        | "A"         | 5g               | true
        30.0g        | "B"         | 10g              | false
        21.0g        | "B"         | 0g               | false
    }

    private static String flightOrderSubmitted(String customerId, IATAAirportCode destination, LocalDate flightDate, TestApplication app) {
        def flightCourseId = rawFlightCourseId(LocalDateTime.of(flightDate, LocalTime.now()).toInstant(ZoneOffset.UTC))
        def orderId = rawOrderId(customerId, flightCourseId)
        final originAirport = rawOriginAirport()
        final destinationAirport = destination.raw()
        def flightsOrderSubmitted = new FlightOrderSubmitted(
                orderId,
                customerId,
                flightCourseId,
                originAirport,
                destinationAirport
        )
        app.eventOccurred(flightsOrderSubmitted)
        orderId
    }

    private static TestApplication discounts(TenantGroups tenantGroups, AppliedDiscountsRegistry appliedDiscountsRegistry, AirportsContinents airportsContinents, CustomersBirthdays customersBirthdays) {
        inMemoryTestApplication(new DiscountsModule(tenantGroups, appliedDiscountsRegistry, new InMemoryFlightOrders(), airportsContinents, customersBirthdays))
    }

    private AirportsContinents airportIsOnContinent(destination, continent) {
        Stub(AirportsContinents) {
            continentOf(destination) >> Optional.of(continent)
        }
    }

    private CustomersBirthdays customerHaveBirthdayOn(String customerId, flightDate) {
        Stub(CustomersBirthdays) {
            forCustomer(CustomerId.fromRaw(customerId)) >> Optional.of(flightDate)
        }
    }
}

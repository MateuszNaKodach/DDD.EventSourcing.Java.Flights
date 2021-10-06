package pl.zycienakodach.pragmaticflights.modules.pricing

import pl.zycienakodach.pragmaticflights.modules.pricing.api.commands.ApplyOrderPriceDiscount
import pl.zycienakodach.pragmaticflights.modules.pricing.api.commands.CalculateOrderTotalPrice
import pl.zycienakodach.pragmaticflights.modules.pricing.api.commands.DefineRegularPrice
import pl.zycienakodach.pragmaticflights.modules.pricing.api.events.CalculateOrderTotalPriceCompleted
import pl.zycienakodach.pragmaticflights.modules.pricing.api.events.CalculateOrderTotalPriceStarted
import pl.zycienakodach.pragmaticflights.modules.pricing.api.events.DiscountApplied
import pl.zycienakodach.pragmaticflights.modules.pricing.api.events.RegularPriceDefined
import pl.zycienakodach.pragmaticflights.sdk.TestApplication
import spock.lang.Specification

import static pl.zycienakodach.pragmaticflights.ApplicationTestFixtures.inMemoryTestApplication
import static pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.flightid.FlightCourseTestFixtures.rawFlightCourseId
import static pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.orderid.OrderIdTestFixtures.rawOrderId
import static pl.zycienakodach.pragmaticflights.sdk.application.EventStreamNameTestFixtures.testTenantEventStream
import static pl.zycienakodach.pragmaticflights.sdk.infrastructure.message.command.CommandTestFixtures.aCommandMetadata

class PricingSpec extends Specification {

    def "define regular price for flight"() {
        given:
        def app = pricing()

        and:
        def flightCourseId = rawFlightCourseId()
        def command = new DefineRegularPrice(
                flightCourseId,
                300.0g
        )
        def metadata = aCommandMetadata()

        when:
        app.execute(command, metadata)

        then:
        app.lastPublishedEvent() == new RegularPriceDefined(flightCourseId, 300.0g)
    }

    def "calculating price"() {
        given:
        def app = pricing()

        and:
        def flightCourseId = rawFlightCourseId()
        def regularPrice = 30.0g
        final regularPriceDefined = new RegularPriceDefined(flightCourseId, regularPrice)
        app.eventOccurred(testTenantEventStream("FlightCourseOrdersPricing", flightCourseId), regularPriceDefined)

        when:
        var orderId = rawOrderId(flightCourseId)
        def calculateOrderPriceCommand = new CalculateOrderTotalPrice(orderId)
        def calculateOrderPriceMetadata = aCommandMetadata()
        app.execute(calculateOrderPriceCommand, calculateOrderPriceMetadata)

        then:
        app.lastEventCausedBy(calculateOrderPriceMetadata.commandId()) == new CalculateOrderTotalPriceStarted(orderId, regularPrice)

        when:
        def applyDiscountCommand = new ApplyOrderPriceDiscount(
                orderId,
                10.0g
        )
        def applyDiscountMetadata = aCommandMetadata()
        app.execute(applyDiscountCommand, applyDiscountMetadata)

        then:
        app.eventsCausedBy(applyDiscountMetadata.commandId()) == [
                new DiscountApplied(orderId, 10.0g),
                new CalculateOrderTotalPriceCompleted(orderId, regularPrice, 10.0g, 20.0g)
        ]
    }

    private static TestApplication pricing() {
        return inMemoryTestApplication(new PricingModule())
    }
}

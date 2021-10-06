package pl.zycienakodach.pragmaticflights.modules.pricing

import pl.zycienakodach.pragmaticflights.modules.pricing.api.commands.ApplyOrderPriceDiscount
import pl.zycienakodach.pragmaticflights.modules.pricing.api.commands.CalculateOrderTotalPrice
import pl.zycienakodach.pragmaticflights.modules.pricing.api.commands.DefineRegularPrice
import pl.zycienakodach.pragmaticflights.modules.pricing.api.events.CalculateOrderTotalPriceCompleted
import pl.zycienakodach.pragmaticflights.modules.pricing.api.events.CalculateOrderTotalPriceStarted
import pl.zycienakodach.pragmaticflights.modules.pricing.api.events.RegularPriceDefined
import pl.zycienakodach.pragmaticflights.sdk.TestApplication
import pl.zycienakodach.pragmaticflights.sdk.infrastructure.message.event.InMemoryEventBus
import pl.zycienakodach.pragmaticflights.sdk.infrastructure.message.event.RecordingEventBus
import spock.lang.Specification

import static pl.zycienakodach.pragmaticflights.ApplicationTestFixtures.inMemoryApplication
import static pl.zycienakodach.pragmaticflights.ApplicationTestFixtures.test
import static pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.flightid.FlightCourseTestFixtures.rawFlightCourseId
import static pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.orderid.OrderIdTestFixtures.rawOrderId
import static pl.zycienakodach.pragmaticflights.sdk.application.EventStreamNameTestFixtures.testTenantEventStream
import static pl.zycienakodach.pragmaticflights.sdk.infrastructure.message.command.CommandTestFixtures.aCommandMetadata

class PricingSpec extends Specification {

    def "define regular price for flight"() {
        given:
        def eventBus = new RecordingEventBus(new InMemoryEventBus())
        def app = pricing(eventBus)

        and:
        def flightCourseId = rawFlightCourseId()
        def command = new DefineRegularPrice(
                flightCourseId,
                300
        )
        def metadata = aCommandMetadata()

        when:
        app.execute(command, metadata)

        then:
        eventBus.lastPublishedEvent() == new RegularPriceDefined(flightCourseId, 300)
    }

    def "calculating price"() {
        given:
        def eventBus = new RecordingEventBus(new InMemoryEventBus())
        def app = pricing(eventBus)

        and:
        def flightCourseId = rawFlightCourseId()
        def regularPrice = 30
        final regularPriceDefined = new RegularPriceDefined(flightCourseId, regularPrice)
        app.eventOccurred(testTenantEventStream("FlightCourseOrdersPricing", flightCourseId), regularPriceDefined)

        when:
        var orderId = rawOrderId(flightCourseId)
        def calculateOrderPriceCommand = new CalculateOrderTotalPrice(orderId)
        def calculateOrderPriceMetadata = aCommandMetadata()
        app.execute(calculateOrderPriceCommand, calculateOrderPriceMetadata)

        then:
        eventBus.lastEventCausedBy(calculateOrderPriceMetadata.commandId()) == new CalculateOrderTotalPriceStarted(orderId, regularPrice)

        when:
        def applyDiscountCommand = new ApplyOrderPriceDiscount(
                orderId,
                10
        )
        def applyDiscountMetadata = aCommandMetadata()
        app.execute(applyDiscountCommand, applyDiscountMetadata)

        then:
        eventBus.lastEventCausedBy(applyDiscountMetadata.commandId()) == new CalculateOrderTotalPriceCompleted(orderId, regularPrice, 10, 20)
    }

    private static TestApplication pricing(RecordingEventBus eventBus) {
        test(inMemoryApplication(eventBus).withModule(new PricingModule()))
    }
}

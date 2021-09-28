package pl.zycienakodach.pragmaticflights.modules.pricing

import pl.zycienakodach.pragmaticflights.modules.pricing.api.commands.ApplyOrderPriceDiscount
import pl.zycienakodach.pragmaticflights.modules.pricing.api.commands.CalculateOrderTotalPrice
import pl.zycienakodach.pragmaticflights.modules.pricing.api.commands.DefineRegularPrice
import pl.zycienakodach.pragmaticflights.modules.pricing.api.events.CalculateOrderTotalPriceCompleted
import pl.zycienakodach.pragmaticflights.modules.pricing.api.events.CalculateOrderTotalPriceStarted
import pl.zycienakodach.pragmaticflights.modules.pricing.api.events.RegularPriceDefined
import pl.zycienakodach.pragmaticflights.modules.pricing.application.RegularPrices
import pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.flightid.FlightId
import pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.money.EuroMoney
import pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.orderid.OrderId
import pl.zycienakodach.pragmaticflights.sdk.Application
import pl.zycienakodach.pragmaticflights.sdk.infrastructure.message.event.InMemoryEventBus
import pl.zycienakodach.pragmaticflights.sdk.infrastructure.message.event.RecordingEventBus
import spock.lang.Specification

import java.time.DayOfWeek
import java.time.LocalDate

import static pl.zycienakodach.pragmaticflights.ApplicationTestFixtures.inMemoryApplication
import static pl.zycienakodach.pragmaticflights.sdk.infrastructure.message.command.CommandTestFixtures.aCommandMetadata

class PricingSpec extends Specification {

    def "define regular price for flight"() {
        given:
        def eventBus = new RecordingEventBus(new InMemoryEventBus())
        def app = pricing(eventBus, Stub(RegularPrices))

        and:
        def flightId = "KLM 12345 BCA";
        def command = new DefineRegularPrice(
                flightId,
                DayOfWeek.MONDAY,
                300
        )
        def metadata = aCommandMetadata()

        when:
        app.execute(command, metadata)

        then:
        eventBus.lastPublishedEvent() == new RegularPriceDefined(flightId, DayOfWeek.MONDAY, 300)
    }

    def "calculating price"() {
        given:
        def eventBus = new RecordingEventBus(new InMemoryEventBus())

        def flightId = FlightId.fromRaw("KLM 12345 BCA")
        def regularPrice = EuroMoney.of(30)
        def regularPrices = Stub(RegularPrices) {
            findBy(flightId, DayOfWeek.FRIDAY) >> Optional.of(regularPrice)
        }
        def app = pricing(eventBus, regularPrices)

        when:
        var orderId = new OrderId("orderId1")
        def calculateOrderPriceCommand = new CalculateOrderTotalPrice(
                orderId.raw(),
                flightId.raw(),
                LocalDate.of(2021, 10, 1)
        )
        def calculateOrderPriceMetadata = aCommandMetadata()
        app.execute(calculateOrderPriceCommand, calculateOrderPriceMetadata)

        then:
        eventBus.lastEventCausedBy(calculateOrderPriceMetadata.commandId()) == new CalculateOrderTotalPriceStarted(orderId.raw(), regularPrice.value())

        when:
        def applyDiscountCommand = new ApplyOrderPriceDiscount(
                orderId.raw(),
                10
        )
        def applyDiscountMetadata = aCommandMetadata()
        app.execute(applyDiscountCommand, applyDiscountMetadata)

        then:
        eventBus.lastEventCausedBy(applyDiscountMetadata.commandId()) == new CalculateOrderTotalPriceCompleted(orderId.raw(), regularPrice.value(), 10, 20)
    }

    private static Application pricing(RecordingEventBus eventBus, RegularPrices regularPrices) {
        inMemoryApplication(eventBus)
                .withModule(new PricingModule(regularPrices))
    }
}

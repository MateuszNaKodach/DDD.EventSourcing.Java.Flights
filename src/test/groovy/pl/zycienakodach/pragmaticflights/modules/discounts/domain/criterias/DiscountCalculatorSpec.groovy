package pl.zycienakodach.pragmaticflights.modules.discounts.domain.criterias

import pl.zycienakodach.pragmaticflights.modules.discounts.domain.RegularPrice
import pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.money.EuroMoney
import spock.lang.Specification

import static pl.zycienakodach.pragmaticflights.modules.discounts.domain.criterias.DiscountCalculatorTestFixtures.discountsCriteriaWith
import static pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.orderid.OrderIdTestFixtures.anOrderId

class DiscountCalculatorSpec extends Specification {

    def "discount calculator should keep minimal price"(double regularPrice, List<Double> discountsToApply, double minimalPrice, double expectedDiscount) {
        given:
        def discounts = discountsCriteriaWith(discountsToApply)
        def calculator = new DiscountCalculator(EuroMoney.of(minimalPrice), discounts)

        when:
        def calculatedDiscount = calculator.calculateDiscount(anOrderId(), new RegularPrice(EuroMoney.of(regularPrice)))

        then:
        calculatedDiscount.euro().toDouble() == expectedDiscount

        where:
        regularPrice | minimalPrice | discountsToApply | expectedDiscount
        30.00d       | 20.00d       | [10.00d]         | 10.00d
        30.00d       | 20.00d       | [20.00d]         | 00.00d
    }
}

//BigDecimal - 3.5g - move to BigDecimal

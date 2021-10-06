package pl.zycienakodach.pragmaticflights.modules.discounts.domain.criterias

import pl.zycienakodach.pragmaticflights.modules.discounts.domain.RegularPrice
import pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.money.EuroMoney
import spock.lang.Specification
import spock.lang.Unroll

import static pl.zycienakodach.pragmaticflights.modules.discounts.domain.criterias.DiscountCalculatorTestFixtures.discountsCriteriaWith
import static pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.orderid.OrderIdTestFixtures.anOrderId

class DiscountCalculatorSpec extends Specification {

    @Unroll
    def "discount calculator should keep minimal price"(
            BigDecimal regularPrice,
            Map<String, BigDecimal> discountsToApply,
            BigDecimal minimalPrice,
            BigDecimal expectedDiscount,
            List<String> expectedAppliedCriteria
    ) {
        given:
        def discounts = discountsCriteriaWith(discountsToApply)
        def calculator = new DiscountCalculator(EuroMoney.of(minimalPrice), discounts)

        when:
        def calculatedDiscount = calculator.calculateDiscount(anOrderId(), new RegularPrice(EuroMoney.of(regularPrice)))

        then:
        calculatedDiscount.euro().toBigDecimal() == expectedDiscount
        calculatedDiscount.appliedCriteria().collect { it.raw() } == expectedAppliedCriteria

        where:
        regularPrice | minimalPrice | discountsToApply                    | expectedDiscount | expectedAppliedCriteria
        30.0g        | 20.0g        | [discount1: 10.0g]                  | 10.0g            | ['discount1']
        30.0g        | 20.0g        | [discount1: 20.0g]                  | 0.0g             | []
        30.0g        | 20.0g        | [discount1: 5.0g, discount2: 5.0g]  | 10.0g            | ['discount1', 'discount2']
        29.0g        | 20.0g        | [discount1: 10.0g, discount2: 5.0g] | 5.0g             | ['discount2']
        21.0g        | 20.0g        | [discount1: 5.0g, discount2: 5.0g]  | 0.0g             | []
    }
}


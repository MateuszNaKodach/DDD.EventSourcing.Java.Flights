package pl.zycienakodach.pragmaticflights.modules.discounts.api.event;

import java.math.BigDecimal;

public record DiscountValueCalculated(String orderId, BigDecimal discountInEuro) implements DiscountsEvent {
}

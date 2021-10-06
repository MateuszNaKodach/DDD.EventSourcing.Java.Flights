package pl.zycienakodach.pragmaticflights.modules.pricing.api.commands;

import java.math.BigDecimal;

public record ApplyOrderPriceDiscount(
    String orderId,
    BigDecimal discountInEuro
) {
}

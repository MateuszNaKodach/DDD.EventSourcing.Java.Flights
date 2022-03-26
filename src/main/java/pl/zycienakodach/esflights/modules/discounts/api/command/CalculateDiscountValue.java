package pl.zycienakodach.esflights.modules.discounts.api.command;

import java.math.BigDecimal;

public record CalculateDiscountValue(String orderId, BigDecimal regularPriceInEuro) {
}

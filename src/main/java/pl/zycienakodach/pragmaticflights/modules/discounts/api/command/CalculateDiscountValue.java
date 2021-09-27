package pl.zycienakodach.pragmaticflights.modules.discounts.api.command;

public record CalculateDiscountValue(String orderId, double regularPriceInEuro) {
}

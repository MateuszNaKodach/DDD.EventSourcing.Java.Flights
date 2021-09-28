package pl.zycienakodach.pragmaticflights.modules.pricing.api.commands;

public record ApplyOrderPriceDiscount(
    String orderId,
    double discountInEuro
) {
}

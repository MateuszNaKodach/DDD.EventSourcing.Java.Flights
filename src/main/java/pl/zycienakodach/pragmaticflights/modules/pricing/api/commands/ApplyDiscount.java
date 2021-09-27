package pl.zycienakodach.pragmaticflights.modules.pricing.api.commands;

public record ApplyDiscount(String orderId,
                            double discountInEuro) {
}

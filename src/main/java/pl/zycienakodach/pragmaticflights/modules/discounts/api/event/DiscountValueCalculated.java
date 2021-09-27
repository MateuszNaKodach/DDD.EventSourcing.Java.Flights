package pl.zycienakodach.pragmaticflights.modules.discounts.api.event;

record DiscountValueCalculated(String orderId, double discountInEuro) {
}

package pl.zycienakodach.esflights.modules.pricing.api.commands;

import java.math.BigDecimal;

public record DefineRegularPrice(
    String flightCourseId,
    BigDecimal priceInEuro
) {
}

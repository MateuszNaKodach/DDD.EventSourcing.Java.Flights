package pl.zycienakodach.pragmaticflights.modules.discounts.infrastructure.customers;

import java.time.LocalDate;

public record CustomerEntity(String customerId, LocalDate dateOfBirth) {
}

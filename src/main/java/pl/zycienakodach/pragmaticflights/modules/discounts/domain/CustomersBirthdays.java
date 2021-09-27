package pl.zycienakodach.pragmaticflights.modules.discounts.domain;

import pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.customerid.CustomerId;

import java.time.LocalDate;
import java.util.Optional;

public interface CustomersBirthdays {

  Optional<LocalDate> forCustomer(CustomerId customerId);

}

package pl.zycienakodach.esflights.modules.discounts.domain.criterias.flightdepartureoncustomerbirthday;

import pl.zycienakodach.esflights.modules.sharedkernel.domain.customerid.CustomerId;

import java.time.LocalDate;
import java.util.Optional;

public interface CustomersBirthdays {

  Optional<LocalDate> forCustomer(CustomerId customerId);

}

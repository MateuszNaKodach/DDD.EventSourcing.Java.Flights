package pl.zycienakodach.pragmaticflights.modules.pricing.domain.discounting;

import pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.customerid.CustomerId;
import pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.flightid.FlightId;

interface DiscountPolicy {

  boolean isApplicable(FlightId flightId, CustomerId customerId);


}

package pl.zycienakodach.pragmaticflights.modules.discounts.domain.criterias.flighttoafricaonthursday;

import pl.zycienakodach.pragmaticflights.modules.discounts.domain.Discount;
import pl.zycienakodach.pragmaticflights.modules.discounts.domain.RegularPrice;
import pl.zycienakodach.pragmaticflights.modules.discounts.domain.criterias.OrderDiscountCriteria;
import pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.orderid.OrderId;

public class FlightToAfricaOnThursdayDiscount implements OrderDiscountCriteria {

  private final AirportsContinents airportsContinents;

  public FlightToAfricaOnThursdayDiscount(AirportsContinents airportsContinents) {
    this.airportsContinents = airportsContinents;
  }

  @Override
  public Discount calculateDiscount(OrderId orderId, RegularPrice regularPrice) {
    return null;
  }

}

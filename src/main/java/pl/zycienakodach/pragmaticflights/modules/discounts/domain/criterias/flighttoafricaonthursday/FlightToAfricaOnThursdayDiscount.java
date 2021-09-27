package pl.zycienakodach.pragmaticflights.modules.discounts.domain.criterias.flighttoafricaonthursday;

import pl.zycienakodach.pragmaticflights.modules.discounts.domain.Discount;
import pl.zycienakodach.pragmaticflights.modules.discounts.domain.RegularPrice;
import pl.zycienakodach.pragmaticflights.modules.discounts.domain.criterias.OrderDiscountCriteria;
import pl.zycienakodach.pragmaticflights.modules.discounts.domain.criterias.Orders;
import pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.money.EuroMoney;
import pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.orderid.OrderId;

import java.time.DayOfWeek;

public class FlightToAfricaOnThursdayDiscount implements OrderDiscountCriteria {

  private final Orders orders;
  private final AirportsContinents airportsContinents;

  public FlightToAfricaOnThursdayDiscount(Orders ordersFlights, AirportsContinents airportsContinents) {
    this.orders = ordersFlights;
    this.airportsContinents = airportsContinents;
  }

  @Override
  public Discount calculateDiscount(OrderId orderId, RegularPrice regularPrice) {
    var order = orders.findByOrderId(orderId).orElseThrow();

    var destinationContinentIsAfrica = airportsContinents.continentOf(order.flight().destination()) == Continent.AFRICA;
    var flightOnThursday = order.flightDate().getDayOfWeek().equals(DayOfWeek.THURSDAY);

    return destinationContinentIsAfrica && flightOnThursday
        ? Discount.just(this, EuroMoney.of(5))
        : Discount.none();
  }

}

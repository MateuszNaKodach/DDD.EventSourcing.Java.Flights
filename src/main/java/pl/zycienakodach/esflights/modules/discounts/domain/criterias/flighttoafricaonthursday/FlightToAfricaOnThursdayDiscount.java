package pl.zycienakodach.esflights.modules.discounts.domain.criterias.flighttoafricaonthursday;

import pl.zycienakodach.esflights.modules.discounts.domain.Discount;
import pl.zycienakodach.esflights.modules.discounts.domain.criterias.DiscountCriteriaName;
import pl.zycienakodach.esflights.modules.discounts.domain.criterias.OrderDiscountCriteria;
import pl.zycienakodach.esflights.modules.discounts.domain.criterias.Orders;
import pl.zycienakodach.esflights.modules.sharedkernel.domain.money.EuroMoney;
import pl.zycienakodach.esflights.modules.sharedkernel.domain.orderid.OrderId;

import java.time.DayOfWeek;

public class FlightToAfricaOnThursdayDiscount implements OrderDiscountCriteria {

  private final Orders orders;
  private final AirportsContinents airportsContinents;

  public FlightToAfricaOnThursdayDiscount(Orders ordersFlights, AirportsContinents airportsContinents) {
    this.orders = ordersFlights;
    this.airportsContinents = airportsContinents;
  }

  @Override
  public Discount calculateDiscount(OrderId orderId) {
    var order = orders.findByOrderId(orderId).orElseThrow();

    var destinationContinent = airportsContinents.continentOf(order.flight().destination());
    if (destinationContinent.isEmpty()) {
      return Discount.none();
    }
    var destinationContinentIsAfrica = destinationContinent.get() == Continent.AFRICA;
    var flightOnThursday = order.flightDate().getDayOfWeek().equals(DayOfWeek.THURSDAY);

    return destinationContinentIsAfrica && flightOnThursday
        ? Discount.just(new DiscountCriteriaName(this.getClass().getSimpleName()), EuroMoney.of(5))
        : Discount.none();
  }

}

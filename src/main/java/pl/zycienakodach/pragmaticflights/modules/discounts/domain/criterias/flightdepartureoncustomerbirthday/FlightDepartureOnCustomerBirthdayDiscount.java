package pl.zycienakodach.pragmaticflights.modules.discounts.domain.criterias.flightdepartureoncustomerbirthday;

import pl.zycienakodach.pragmaticflights.modules.discounts.domain.Discount;
import pl.zycienakodach.pragmaticflights.modules.discounts.domain.RegularPrice;
import pl.zycienakodach.pragmaticflights.modules.discounts.domain.criterias.OrderDiscountCriteria;
import pl.zycienakodach.pragmaticflights.modules.discounts.domain.criterias.Orders;
import pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.money.EuroMoney;
import pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.orderid.OrderId;

public class FlightDepartureOnCustomerBirthdayDiscount implements OrderDiscountCriteria {

  private final Orders orders;
  private final CustomersBirthdays customersBirthdays;

  public FlightDepartureOnCustomerBirthdayDiscount(Orders orders, CustomersBirthdays customersBirthdays) {
    this.orders = orders;
    this.customersBirthdays = customersBirthdays;
  }

  @Override
  public Discount calculateDiscount(OrderId orderId, RegularPrice regularPrice) {
    var order = orders.findByOrderId(orderId).orElseThrow();
    var customerBirthDate = customersBirthdays.forCustomer(order.customerId()).orElseThrow();
    return customerBirthDate.equals(order.flightDate())
        ? Discount.just(this, EuroMoney.of(50))
        : Discount.none();
  }
}

package pl.zycienakodach.esflights.modules.discounts.domain.criterias.flightdepartureoncustomerbirthday;

import pl.zycienakodach.esflights.modules.discounts.domain.Discount;
import pl.zycienakodach.esflights.modules.discounts.domain.criterias.DiscountCriteriaName;
import pl.zycienakodach.esflights.modules.discounts.domain.criterias.FlightOrder;
import pl.zycienakodach.esflights.modules.discounts.domain.criterias.OrderDiscountCriteria;
import pl.zycienakodach.esflights.modules.discounts.domain.criterias.Orders;
import pl.zycienakodach.esflights.modules.sharedkernel.domain.money.EuroMoney;
import pl.zycienakodach.esflights.modules.sharedkernel.domain.orderid.OrderId;

import java.time.LocalDate;
import java.util.function.Predicate;

public class FlightDepartureOnCustomerBirthdayDiscount implements OrderDiscountCriteria {

  private final Orders orders;
  private final CustomersBirthdays customersBirthdays;

  public FlightDepartureOnCustomerBirthdayDiscount(Orders orders, CustomersBirthdays customersBirthdays) {
    this.orders = orders;
    this.customersBirthdays = customersBirthdays;
  }

  @Override
  public Discount calculateDiscount(OrderId orderId) {
    var order = orders.findByOrderId(orderId).orElseThrow();

    var customerBirthDate = customersBirthdays.forCustomer(order.customerId());
    return customerBirthDate
        .filter(birthdayOnFlightDate(order))
        .map((__) -> Discount.just(new DiscountCriteriaName(this.getClass().getSimpleName()), EuroMoney.of(5)))
        .orElse(Discount.none());
  }

  private Predicate<LocalDate> birthdayOnFlightDate(FlightOrder order) {
    return birthDate -> (birthDate.getMonthValue() == (order.flightDate().getMonthValue())) && (birthDate.getDayOfMonth() == order.flightDate().getDayOfMonth());
  }
}

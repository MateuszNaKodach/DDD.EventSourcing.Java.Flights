package pl.zycienakodach.pragmaticflights.modules.discounts;

import pl.zycienakodach.pragmaticflights.modules.discounts.api.command.CalculateDiscountValue;
import pl.zycienakodach.pragmaticflights.modules.discounts.domain.Discount;
import pl.zycienakodach.pragmaticflights.modules.discounts.domain.RegularPrice;
import pl.zycienakodach.pragmaticflights.modules.discounts.domain.criterias.DiscountCalculator;
import pl.zycienakodach.pragmaticflights.modules.discounts.domain.criterias.Orders;
import pl.zycienakodach.pragmaticflights.modules.discounts.domain.criterias.flightdepartureoncustomerbirthday.CustomersBirthdays;
import pl.zycienakodach.pragmaticflights.modules.discounts.domain.criterias.flightdepartureoncustomerbirthday.FlightDepartureOnCustomerBirthdayDiscount;
import pl.zycienakodach.pragmaticflights.modules.discounts.domain.criterias.flighttoafricaonthursday.AirportsContinents;
import pl.zycienakodach.pragmaticflights.modules.discounts.domain.criterias.flighttoafricaonthursday.FlightToAfricaOnThursdayDiscount;
import pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.money.EuroMoney;
import pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.orderid.OrderId;
import pl.zycienakodach.pragmaticflights.sdk.Application;
import pl.zycienakodach.pragmaticflights.sdk.ApplicationModule;
import pl.zycienakodach.pragmaticflights.sdk.application.EventStreamName;

import java.util.List;

import static pl.zycienakodach.pragmaticflights.modules.discounts.domain.Discounting.calculateDiscount;
import static pl.zycienakodach.pragmaticflights.sdk.application.EventStreamName.category;
import static pl.zycienakodach.pragmaticflights.sdk.application.EventStreamName.streamId;

public class DiscountsModule implements ApplicationModule {

  private final Orders orders;
  private final AirportsContinents airportsContinents;
  private final CustomersBirthdays customersBirthdays;

  DiscountsModule(Orders orders, AirportsContinents airportsContinents, CustomersBirthdays customersBirthdays) {
    this.orders = orders;
    this.airportsContinents = airportsContinents;
    this.customersBirthdays = customersBirthdays;
  }

  @Override
  public ApplicationModule configure(Application app) {
    var discountCalculator = new DiscountCalculator(
        new EuroMoney(20),
        List.of(
            new FlightDepartureOnCustomerBirthdayDiscount(orders, customersBirthdays),
            new FlightToAfricaOnThursdayDiscount(orders, airportsContinents)
        )
    );
    app.onCommand(
        CalculateDiscountValue.class,
        (c, m) -> new EventStreamName(category(m.tenantId().raw(), "DiscountValue"), streamId(c.orderId())),
        (c) -> {
          var orderId = new OrderId(c.orderId());
          var calculatedDiscount = discountCalculator
              .calculateDiscount(orderId, new RegularPrice(new EuroMoney(c.regularPriceInEuro())));
          return calculateDiscount(
              orderId,
              calculatedDiscount
          );
        }
    );
    return null;
  }
}
package pl.zycienakodach.pragmaticflights.modules.discounts;

import pl.zycienakodach.pragmaticflights.modules.discounts.api.command.CalculateDiscountValue;
import pl.zycienakodach.pragmaticflights.modules.discounts.application.AppliedDiscountsRegistry;
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
import pl.zycienakodach.pragmaticflights.sdk.application.eventstream.EventStreamName;
import pl.zycienakodach.pragmaticflights.sdk.application.tenant.TenantGroupId;
import pl.zycienakodach.pragmaticflights.sdk.application.tenant.TenantGroups;
import pl.zycienakodach.pragmaticflights.sdk.application.tenant.TenantId;

import java.util.List;

import static pl.zycienakodach.pragmaticflights.modules.discounts.domain.Discounting.calculateDiscount;

public class DiscountsModule implements ApplicationModule {

  private final TenantGroups tenantGroups;
  private final AppliedDiscountsRegistry appliedDiscountsRegistry;
  private final Orders orders;
  private final AirportsContinents airportsContinents;
  private final CustomersBirthdays customersBirthdays;

  public DiscountsModule(
      TenantGroups tenantGroups,
      AppliedDiscountsRegistry appliedDiscountsRegistry,
      Orders orders,
      AirportsContinents airportsContinents,
      CustomersBirthdays customersBirthdays
  ) {
    this.tenantGroups = tenantGroups;
    this.appliedDiscountsRegistry = appliedDiscountsRegistry;
    this.orders = orders;
    this.airportsContinents = airportsContinents;
    this.customersBirthdays = customersBirthdays;
  }

  @Override
  public ApplicationModule configure(Application app) {
    var discountCalculator = new DiscountCalculator(
        EuroMoney.of(20),
        List.of(
            new FlightDepartureOnCustomerBirthdayDiscount(orders, customersBirthdays),
            new FlightToAfricaOnThursdayDiscount(orders, airportsContinents)
        )
    );
    app.onCommand(
        CalculateDiscountValue.class,
        (c, m) -> EventStreamName.ofCategory("Discount").withId(c.orderId()),
        (c, m) -> {
          var orderId = OrderId.fromRaw(c.orderId());
          var calculatedDiscount = discountCalculator
              .calculateDiscount(orderId, new RegularPrice(EuroMoney.of(c.regularPriceInEuro())));
          var result = calculateDiscount(
              orderId,
              calculatedDiscount
          );

          runForTenantGroup(
              m.tenantId(),
              TenantGroupId.of("A"),
              () -> appliedDiscountsRegistry.save(orderId, calculatedDiscount.appliedCriteria())
          );

          return result;
        }
    );
    return this;
  }

  private void runForTenantGroup(TenantId tenantId, TenantGroupId tenantGroupId, Runnable function) {
    var tenantGroup = tenantGroups.tenantGroupOf(tenantId);
    if (tenantGroup.equals(tenantGroupId)) {
      function.run();
    }
  }
}

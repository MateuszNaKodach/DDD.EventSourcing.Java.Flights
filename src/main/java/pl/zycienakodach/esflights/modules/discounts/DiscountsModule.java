package pl.zycienakodach.esflights.modules.discounts;

import pl.zycienakodach.esflights.modules.discounts.api.command.CalculateDiscountValue;
import pl.zycienakodach.esflights.modules.discounts.application.AppliedDiscountsRegistry;
import pl.zycienakodach.esflights.modules.discounts.domain.RegularPrice;
import pl.zycienakodach.esflights.modules.discounts.domain.criterias.DiscountCalculator;
import pl.zycienakodach.esflights.modules.discounts.domain.criterias.flightdepartureoncustomerbirthday.CustomersBirthdays;
import pl.zycienakodach.esflights.modules.discounts.domain.criterias.flightdepartureoncustomerbirthday.FlightDepartureOnCustomerBirthdayDiscount;
import pl.zycienakodach.esflights.modules.discounts.domain.criterias.flighttoafricaonthursday.AirportsContinents;
import pl.zycienakodach.esflights.modules.discounts.domain.criterias.flighttoafricaonthursday.FlightToAfricaOnThursdayDiscount;
import pl.zycienakodach.esflights.modules.discounts.infrastructure.flightorders.FlightOrdersProjectionAdapter;
import pl.zycienakodach.esflights.modules.discounts.infrastructure.flightorders.FlightOrdersRepository;
import pl.zycienakodach.esflights.modules.discounts.infrastructure.flightorders.FlightsOrdersProjection;
import pl.zycienakodach.esflights.modules.sharedkernel.domain.money.EuroMoney;
import pl.zycienakodach.esflights.modules.sharedkernel.domain.orderid.OrderId;
import pl.zycienakodach.esflights.sdk.Application;
import pl.zycienakodach.esflights.sdk.ApplicationModule;
import pl.zycienakodach.esflights.sdk.application.eventstream.EventStreamName;
import pl.zycienakodach.esflights.sdk.application.tenant.TenantGroupId;
import pl.zycienakodach.esflights.sdk.application.tenant.TenantGroups;
import pl.zycienakodach.esflights.sdk.application.tenant.TenantId;

import java.util.List;

import static pl.zycienakodach.esflights.modules.discounts.domain.Discounting.calculateDiscount;

public class DiscountsModule implements ApplicationModule {

  private final TenantGroups tenantGroups;
  private final AppliedDiscountsRegistry appliedDiscountsRegistry;
  private final FlightOrdersRepository flightOrdersRepository;
  private final AirportsContinents airportsContinents;
  private final CustomersBirthdays customersBirthdays;

  public DiscountsModule(
      TenantGroups tenantGroups,
      AppliedDiscountsRegistry appliedDiscountsRegistry,
      FlightOrdersRepository flightOrdersRepository,
      AirportsContinents airportsContinents,
      CustomersBirthdays customersBirthdays
  ) {
    this.tenantGroups = tenantGroups;
    this.appliedDiscountsRegistry = appliedDiscountsRegistry;
    this.flightOrdersRepository = flightOrdersRepository;
    this.airportsContinents = airportsContinents;
    this.customersBirthdays = customersBirthdays;
  }

  @Override
  public ApplicationModule configure(Application app) {
    var orders = new FlightOrdersProjectionAdapter(flightOrdersRepository);
    configureOrdersProjection(app);

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

  private void configureOrdersProjection(Application app) {
    var projection = new FlightsOrdersProjection(flightOrdersRepository);
    projection.configure(app);
  }

  private void runForTenantGroup(TenantId tenantId, TenantGroupId tenantGroupId, Runnable function) {
    var tenantGroup = tenantGroups.tenantGroupOf(tenantId);
    if (tenantGroup.equals(tenantGroupId)) {
      function.run();
    }
  }
}

package pl.zycienakodach.esflights;

import pl.zycienakodach.esflights.modules.discounts.DiscountsModule;
import pl.zycienakodach.esflights.modules.discounts.domain.criterias.flighttoafricaonthursday.Continent;
import pl.zycienakodach.esflights.modules.discounts.infrastructure.airportscontinents.InMemoryAirportsContinents;
import pl.zycienakodach.esflights.modules.discounts.infrastructure.applieddiscountsregistry.InMemoryAppliedDiscountsRegistry;
import pl.zycienakodach.esflights.modules.discounts.infrastructure.customers.CustomerEntity;
import pl.zycienakodach.esflights.modules.discounts.infrastructure.customers.InMemoryCustomerRepository;
import pl.zycienakodach.esflights.modules.discounts.infrastructure.flightorders.FlightsOrdersProjection;
import pl.zycienakodach.esflights.modules.discounts.infrastructure.flightorders.InMemoryFlightOrders;
import pl.zycienakodach.esflights.modules.flightsschedule.FlightsScheduleModule;
import pl.zycienakodach.esflights.modules.ordering.OrderingModule;
import pl.zycienakodach.esflights.modules.pricing.PricingModule;
import pl.zycienakodach.esflights.modules.sharedkernel.domain.flightid.FlightIdFactory;
import pl.zycienakodach.esflights.modules.sharedkernel.domain.iata.IATAAirlinesCodeFactory;
import pl.zycienakodach.esflights.modules.sharedkernel.domain.iata.IATAAirportCodeFactory;
import pl.zycienakodach.esflights.processes.calculatingorderprice.CalculatingOrderTotalPriceProcess;
import pl.zycienakodach.esflights.processes.defaultflightprice.DefaultFlightPriceProcess;
import pl.zycienakodach.esflights.processes.sellingscheduledflights.SellingScheduledFlightsProcess;
import pl.zycienakodach.esflights.sdk.Application;
import pl.zycienakodach.esflights.sdk.EventDrivenApplication;
import pl.zycienakodach.esflights.sdk.application.idgenerator.IdGenerator;
import pl.zycienakodach.esflights.sdk.application.tenant.TenantGroupId;
import pl.zycienakodach.esflights.sdk.application.tenant.TenantId;
import pl.zycienakodach.esflights.sdk.application.time.TimeProvider;
import pl.zycienakodach.esflights.sdk.infrastructure.eventstore.InMemoryEventStore;
import pl.zycienakodach.esflights.sdk.infrastructure.message.command.InMemoryCommandBus;
import pl.zycienakodach.esflights.sdk.infrastructure.message.event.InMemoryEventBus;
import pl.zycienakodach.esflights.sdk.infrastructure.service.EventStoreApplicationService;
import pl.zycienakodach.esflights.sdk.infrastructure.tenant.InMemoryTenantRegistry;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

class Main {

  public static void main(String[] args) {
    var clock = Clock.systemUTC();
    final TimeProvider timeProvider = clock::instant;
    var app = withAllModules(inMemoryApplication(), timeProvider);
    app.init();
  }

  public static Application inMemoryApplication() {
    var commandBus = new InMemoryCommandBus();
    var eventBus = new InMemoryEventBus();
    var eventStore = new InMemoryEventStore(eventBus);
    IdGenerator idGenerator = () -> UUID.randomUUID().toString();
    var clock = Clock.systemUTC();
    TimeProvider timeProvider = clock::instant;

    var applicationService = new EventStoreApplicationService(eventStore, idGenerator, timeProvider);
    return new EventDrivenApplication(commandBus, eventStore, applicationService, idGenerator, timeProvider);
  }

  private static Application withAllModules(Application app, TimeProvider timeProvider) {
    var flightOrdersRepository = new InMemoryFlightOrders();
    var customerRepository = new InMemoryCustomerRepository(
        Set.of(
            new CustomerEntity("customer1", LocalDate.of(1996, 8, 23)),
            new CustomerEntity("customer2", LocalDate.of(2012, 12, 25))
        )
    );
    var airportsContinents = new InMemoryAirportsContinents(
        Map.ofEntries(
            Map.entry("BKC", Continent.NORTH_AMERICA),
            Map.entry("FMA", Continent.SOUTH_AMERICA),
            Map.entry("BVE", Continent.EUROPE),
            Map.entry("HEK", Continent.ASIA),
            Map.entry("NBO", Continent.AFRICA),
            Map.entry("PNA", Continent.EUROPE),
            Map.entry("LCY", Continent.EUROPE),
            Map.entry("BVI", Continent.AUSTRALIA),
            Map.entry("BCA", Continent.NORTH_AMERICA)
        )
    );
    var tenantsGroups = new InMemoryTenantRegistry(
        Map.ofEntries(
            Map.entry(new TenantId("Tenant1"), new TenantGroupId("A")),
            Map.entry(new TenantId("Tenant2"), new TenantGroupId("B")),
            Map.entry(new TenantId("Tenant3"), new TenantGroupId("A")),
            Map.entry(new TenantId("Tenant4"), new TenantGroupId("B"))
        )
    );
    var appliedDiscountsRegistry = new InMemoryAppliedDiscountsRegistry();
    return app
        .withModules(List.of(
            new FlightsScheduleModule(timeProvider, new FlightIdFactory(new IATAAirlinesCodeFactory((__) -> true)), new IATAAirportCodeFactory((__) -> true)),
            new OrderingModule(timeProvider),
            new PricingModule(),
            new DiscountsModule(tenantsGroups, appliedDiscountsRegistry, flightOrdersRepository, airportsContinents, customerRepository),
            new DefaultFlightPriceProcess(30),
            new SellingScheduledFlightsProcess(),
            new CalculatingOrderTotalPriceProcess(),
            new FlightsOrdersProjection(flightOrdersRepository) // todo: use just in discounts
        ));
  }


}

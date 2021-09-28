package pl.zycienakodach.pragmaticflights;

import pl.zycienakodach.pragmaticflights.modules.discounts.DiscountsModule;
import pl.zycienakodach.pragmaticflights.modules.discounts.domain.criterias.flighttoafricaonthursday.Continent;
import pl.zycienakodach.pragmaticflights.modules.discounts.infrastructure.airportscontinents.InMemoryAirportsContinents;
import pl.zycienakodach.pragmaticflights.modules.discounts.infrastructure.applieddiscountsregistry.InMemoryAppliedDiscountsRegistry;
import pl.zycienakodach.pragmaticflights.modules.discounts.infrastructure.customers.CustomerEntity;
import pl.zycienakodach.pragmaticflights.modules.discounts.infrastructure.customers.InMemoryCustomerRepository;
import pl.zycienakodach.pragmaticflights.modules.discounts.infrastructure.flightorders.FlightOrdersReadModelAdapter;
import pl.zycienakodach.pragmaticflights.modules.flightsschedule.FlightsScheduleModule;
import pl.zycienakodach.pragmaticflights.modules.ordering.OrderingModule;
import pl.zycienakodach.pragmaticflights.modules.ordering.infrastructure.FlightOffersReadModelAdapter;
import pl.zycienakodach.pragmaticflights.modules.pricing.PricingModule;
import pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.flightid.FlightIdFactory;
import pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.iata.IATAAirlinesCodeFactory;
import pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.iata.IATAAirportCode;
import pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.iata.IATAAirportCodeFactory;
import pl.zycienakodach.pragmaticflights.processes.calculatingorderprice.CalculatingOrderTotalPriceProcess;
import pl.zycienakodach.pragmaticflights.processes.defaultflightprice.DefaultFlightPriceProcess;
import pl.zycienakodach.pragmaticflights.processes.sellingscheduledflights.SellingScheduledFlightsProcess;
import pl.zycienakodach.pragmaticflights.readmodels.flightoffers.FlightsOffersReadModel;
import pl.zycienakodach.pragmaticflights.readmodels.flightoffers.infrastructure.InMemoryFlightOffers;
import pl.zycienakodach.pragmaticflights.readmodels.flightorders.FlightsOrdersReadModel;
import pl.zycienakodach.pragmaticflights.readmodels.flightorders.infrastructure.InMemoryFlightOrders;
import pl.zycienakodach.pragmaticflights.sdk.Application;
import pl.zycienakodach.pragmaticflights.sdk.application.IdGenerator;
import pl.zycienakodach.pragmaticflights.sdk.application.message.event.EventBus;
import pl.zycienakodach.pragmaticflights.sdk.application.tenant.TenantGroupId;
import pl.zycienakodach.pragmaticflights.sdk.application.tenant.TenantId;
import pl.zycienakodach.pragmaticflights.sdk.application.time.TimeProvider;
import pl.zycienakodach.pragmaticflights.sdk.infrastructure.EventStoreApplicationService;
import pl.zycienakodach.pragmaticflights.sdk.infrastructure.eventstore.InMemoryEventStore;
import pl.zycienakodach.pragmaticflights.sdk.infrastructure.message.command.InMemoryCommandBus;
import pl.zycienakodach.pragmaticflights.sdk.infrastructure.message.event.InMemoryEventBus;
import pl.zycienakodach.pragmaticflights.sdk.infrastructure.tenant.InMemoryTenantRegistry;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class ApplicationTestFixtures {

  private ApplicationTestFixtures() {
  }

  public static Application inMemoryApplication() {
    var eventBus = new InMemoryEventBus();
    return inMemoryApplication(eventBus);
  }

  public static Application withAllModules(Application app) {
    TimeProvider timeProvider = Instant::now;

    var flightOffersRepository = new InMemoryFlightOffers();
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
            new FlightsScheduleModule(new FlightIdFactory(new IATAAirlinesCodeFactory((__) -> true)), new IATAAirportCodeFactory((__) -> true)),
            new OrderingModule(new FlightOffersReadModelAdapter(flightOffersRepository), timeProvider),
            new PricingModule(),
            new DiscountsModule(tenantsGroups, appliedDiscountsRegistry, new FlightOrdersReadModelAdapter(flightOrdersRepository), airportsContinents, customerRepository),
            new DefaultFlightPriceProcess(30),
            new SellingScheduledFlightsProcess(),
            new CalculatingOrderTotalPriceProcess(),
            new FlightsOffersReadModel(flightOffersRepository),
            new FlightsOrdersReadModel(flightOrdersRepository)
        ));
  }

  public static Application inMemoryApplication(EventBus eventBus) {
    var commandBus = new InMemoryCommandBus();
    var eventStore = new InMemoryEventStore(eventBus);
    IdGenerator idGenerator = () -> UUID.randomUUID().toString();
    var clock = Clock.systemUTC();
    TimeProvider timeProvider = clock::instant;

    var applicationService = new EventStoreApplicationService(eventStore, idGenerator, timeProvider);
    return new Application(commandBus, eventStore, applicationService, idGenerator);
  }

}

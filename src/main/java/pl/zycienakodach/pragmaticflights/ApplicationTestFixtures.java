package pl.zycienakodach.pragmaticflights;

import pl.zycienakodach.pragmaticflights.modules.discounts.DiscountsModule;
import pl.zycienakodach.pragmaticflights.modules.flightsschedule.FlightsScheduleModule;
import pl.zycienakodach.pragmaticflights.modules.ordering.OrderingModule;
import pl.zycienakodach.pragmaticflights.modules.ordering.infrastructure.offers.InMemoryFlightOffers;
import pl.zycienakodach.pragmaticflights.modules.pricing.PricingModule;
import pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.flightid.FlightIdFactory;
import pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.iata.IATAAirlinesCodeFactory;
import pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.iata.IATAAirportCodeFactory;
import pl.zycienakodach.pragmaticflights.processes.calculatingorderprice.CalculatingOrderTotalPriceProcess;
import pl.zycienakodach.pragmaticflights.processes.defaultflightprice.DefaultFlightPriceProcess;
import pl.zycienakodach.pragmaticflights.processes.sellingscheduledflights.SellingScheduledFlightsProcess;
import pl.zycienakodach.pragmaticflights.sdk.Application;
import pl.zycienakodach.pragmaticflights.sdk.application.IdGenerator;
import pl.zycienakodach.pragmaticflights.sdk.application.message.event.EventBus;
import pl.zycienakodach.pragmaticflights.sdk.application.time.TimeProvider;
import pl.zycienakodach.pragmaticflights.sdk.infrastructure.EventStoreApplicationService;
import pl.zycienakodach.pragmaticflights.sdk.infrastructure.message.command.InMemoryCommandBus;
import pl.zycienakodach.pragmaticflights.sdk.infrastructure.message.event.InMemoryEventBus;
import pl.zycienakodach.pragmaticflights.sdk.infrastructure.eventstore.InMemoryEventStore;

import java.time.Clock;
import java.time.Instant;
import java.util.List;
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

    return app
        .withModules(List.of(
            new FlightsScheduleModule(new FlightIdFactory(new IATAAirlinesCodeFactory((__) -> true)), new IATAAirportCodeFactory((__) -> true)),
            new OrderingModule(new InMemoryFlightOffers(), timeProvider),
            new PricingModule(),
            new DiscountsModule(),
            new DefaultFlightPriceProcess(30),
            new SellingScheduledFlightsProcess(),
            new CalculatingOrderTotalPriceProcess()
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

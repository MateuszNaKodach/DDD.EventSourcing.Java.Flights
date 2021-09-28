package pl.zycienakodach.pragmaticflights.readmodels.regularprices;

import pl.zycienakodach.pragmaticflights.modules.pricing.api.events.RegularPriceDefined;
import pl.zycienakodach.pragmaticflights.readmodels.regularprices.api.FlightRegularPriceRepository;
import pl.zycienakodach.pragmaticflights.sdk.Application;
import pl.zycienakodach.pragmaticflights.sdk.ApplicationModule;

public class FlightRegularPricesReadModel implements ApplicationModule {

  private final FlightRegularPriceRepository flightRegularPriceRepository;

  public FlightRegularPricesReadModel(FlightRegularPriceRepository flightRegularPriceRepository) {
    this.flightRegularPriceRepository = flightRegularPriceRepository;
  }

  @Override
  public ApplicationModule configure(Application app) {
    app.when(RegularPriceDefined.class, (e, __) -> {
      flightRegularPriceRepository.save(
          e.flightId(),
          e.dayOfWeek(),
          e.priceInEuro()
      );
    });
    return this;
  }
}

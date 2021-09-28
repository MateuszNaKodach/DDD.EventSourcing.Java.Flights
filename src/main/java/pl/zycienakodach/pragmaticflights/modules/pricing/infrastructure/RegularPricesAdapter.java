package pl.zycienakodach.pragmaticflights.modules.pricing.infrastructure;

import pl.zycienakodach.pragmaticflights.modules.pricing.application.RegularPrices;
import pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.flightid.FlightId;
import pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.money.EuroMoney;
import pl.zycienakodach.pragmaticflights.readmodels.regularprices.api.FlightRegularPriceRepository;

import java.time.DayOfWeek;
import java.util.Optional;

public class RegularPricesAdapter implements RegularPrices {

  private final FlightRegularPriceRepository repository;

  public RegularPricesAdapter(FlightRegularPriceRepository repository) {
    this.repository = repository;
  }

  @Override
  public Optional<EuroMoney> findBy(FlightId flightId, DayOfWeek dayOfWeek) {
    return repository.findBy(flightId.raw(), dayOfWeek)
        .map(EuroMoney::of);
  }
}

package pl.zycienakodach.pragmaticflights.modules.pricing.application;

import pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.flightid.FlightId;
import pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.money.EuroMoney;

import java.time.DayOfWeek;
import java.util.Optional;

public interface RegularPrices {

  Optional<EuroMoney> findBy(FlightId flightId, DayOfWeek dayOfWeek);
}

package pl.zycienakodach.pragmaticflights.modules.discounts.domain.criterias.flighttoafricaonthursday;

import pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.iata.IATAAirportCode;

import java.util.Optional;

public interface AirportsContinents {

  Optional<Continent> continentOf(IATAAirportCode airportCode);

}

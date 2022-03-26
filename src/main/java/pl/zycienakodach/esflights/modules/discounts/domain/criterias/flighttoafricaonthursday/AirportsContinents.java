package pl.zycienakodach.esflights.modules.discounts.domain.criterias.flighttoafricaonthursday;

import pl.zycienakodach.esflights.modules.sharedkernel.domain.iata.IATAAirportCode;

import java.util.Optional;

public interface AirportsContinents {

  Optional<Continent> continentOf(IATAAirportCode airportCode);

}

package pl.zycienakodach.pragmaticflights.modules.discounts.domain.criterias.flighttoafricaonthursday;

import pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.iata.IATAAirportCode;

public interface AirportsContinents {

  Continent continentOf(IATAAirportCode airportCode);

}

package pl.zycienakodach.esflights.modules.discounts.infrastructure.airportscontinents;

import pl.zycienakodach.esflights.modules.discounts.domain.criterias.flighttoafricaonthursday.AirportsContinents;
import pl.zycienakodach.esflights.modules.discounts.domain.criterias.flighttoafricaonthursday.Continent;
import pl.zycienakodach.esflights.modules.sharedkernel.domain.iata.IATAAirportCode;

import java.util.Map;
import java.util.Optional;

public class InMemoryAirportsContinents implements AirportsContinents {

  private final Map<String, Continent> airportContinent;

  public InMemoryAirportsContinents(Map<String, Continent> airportContinent) {
    this.airportContinent = airportContinent;
  }

  @Override
  public Optional<Continent> continentOf(IATAAirportCode airportCode) {
    return Optional.ofNullable(this.airportContinent.getOrDefault(airportCode.raw(), null));
  }
}

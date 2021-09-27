package pl.zycienakodach.pragmaticflights.modules.discounts.infrastructure.airportscontinents;

import pl.zycienakodach.pragmaticflights.modules.discounts.domain.criterias.flighttoafricaonthursday.AirportsContinents;
import pl.zycienakodach.pragmaticflights.modules.discounts.domain.criterias.flighttoafricaonthursday.Continent;
import pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.iata.IATAAirportCode;

import java.util.concurrent.ConcurrentHashMap;

public class InMemoryAirportsContinents implements AirportsContinents {

  private final ConcurrentHashMap<IATAAirportCode, Continent> airportContinent;


  InMemoryAirportsContinents(ConcurrentHashMap<IATAAirportCode, Continent> airportContinent) {
    this.airportContinent = airportContinent;
  }

  @Override
  public Continent continentOf(IATAAirportCode airportCode) {
    return this.airportContinent.get(airportCode);
  }
}

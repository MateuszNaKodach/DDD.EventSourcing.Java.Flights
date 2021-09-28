package pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.iata;

import pl.zycienakodach.pragmaticflights.modules.discounts.domain.criterias.flighttoafricaonthursday.AirportsContinents;
import pl.zycienakodach.pragmaticflights.modules.discounts.domain.criterias.flighttoafricaonthursday.Continent;
import pl.zycienakodach.pragmaticflights.modules.discounts.infrastructure.airportscontinents.InMemoryAirportsContinents;

import java.util.Map;

public class IATAAirportsCodeFixtures {

  public static IATAAirportCode bucklandAirportAlaskaUnitedStatesNorthAmerica() {
    return IATAAirportCode.fromRaw("BKC");
  }

  public static IATAAirportCode jomoKenyattaInternationalAirportNairobiKenyaAfrica() {
    return IATAAirportCode.fromRaw("NBO");
  }

  public static IATAAirportCode londonCityAirportLondonEnglandEurope() {
    return IATAAirportCode.fromRaw("LCY");
  }

  public static AirportsContinents airportsContinents() {
    return new InMemoryAirportsContinents(
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
  }
}

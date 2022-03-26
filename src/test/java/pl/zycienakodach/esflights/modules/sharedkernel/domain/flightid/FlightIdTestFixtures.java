package pl.zycienakodach.esflights.modules.sharedkernel.domain.flightid;

import pl.zycienakodach.esflights.modules.sharedkernel.domain.iata.IATAAirlinesCodeFactory;

public class FlightIdTestFixtures {

  public static FlightId aFlightId() {
    IATAAirlinesCodeFactory iataAirlinesCodeFactory = new IATAAirlinesCodeFactory((__) -> true);
    var airlines = iataAirlinesCodeFactory.code("KLM");
    return new FlightId(airlines, new FlightNumber(1, 2, 3, 4, 5), new FlightPostfix('A', 'B', 'C'));
  }

  public static String rawFlightId() {
   return aFlightId().raw();
  }

}

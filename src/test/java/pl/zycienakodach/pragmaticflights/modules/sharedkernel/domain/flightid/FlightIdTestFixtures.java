package pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.flightid;

import pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.flightid.FlightId;
import pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.flightid.FlightNumber;
import pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.flightid.FlightPostfix;
import pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.iata.IATAAirlinesCodeFactory;

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

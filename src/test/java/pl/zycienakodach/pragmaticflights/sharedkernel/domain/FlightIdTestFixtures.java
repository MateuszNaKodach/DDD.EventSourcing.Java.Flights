package pl.zycienakodach.pragmaticflights.sharedkernel.domain;

import pl.zycienakodach.pragmaticflights.sharedkernel.domain.flightid.FlightId;
import pl.zycienakodach.pragmaticflights.sharedkernel.domain.flightid.FlightNumber;
import pl.zycienakodach.pragmaticflights.sharedkernel.domain.flightid.FlightPostfix;
import pl.zycienakodach.pragmaticflights.sharedkernel.domain.iata.IATAAirlinesCodeFactory;

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

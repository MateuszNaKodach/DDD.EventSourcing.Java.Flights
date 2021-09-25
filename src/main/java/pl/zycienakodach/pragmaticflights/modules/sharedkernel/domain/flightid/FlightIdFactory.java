package pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.flightid;

import pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.iata.IATAAirlinesCode;
import pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.iata.IATAAirlinesCodeFactory;

public class FlightIdFactory {

  private final IATAAirlinesCodeFactory iataAirlinesCodeFactory;

  public FlightIdFactory(IATAAirlinesCodeFactory iataAirlinesCodeFactory) {
    this.iataAirlinesCodeFactory = iataAirlinesCodeFactory;
  }

  //TODO: complete implementation
  public FlightId flightId(String rawFlightId) {
    var flightIdParts = rawFlightId.split(" ");
    var rawAirlinesCode = flightIdParts[0];
    var rawNumber = flightIdParts[1];
    var rawPostfix = flightIdParts[2];
    IATAAirlinesCode airlines = iataAirlinesCodeFactory.code(rawAirlinesCode);
    return new FlightId(airlines, new FlightNumber(1,2,3,4,5), new FlightPostfix('A','B','C'));
  }

}

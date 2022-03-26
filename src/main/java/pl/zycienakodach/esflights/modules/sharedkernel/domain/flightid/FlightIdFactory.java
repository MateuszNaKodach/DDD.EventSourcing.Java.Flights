package pl.zycienakodach.esflights.modules.sharedkernel.domain.flightid;

import pl.zycienakodach.esflights.modules.sharedkernel.domain.iata.IATAAirlinesCodeFactory;

public class FlightIdFactory {

  private final IATAAirlinesCodeFactory iataAirlinesCodeFactory;

  public FlightIdFactory(IATAAirlinesCodeFactory iataAirlinesCodeFactory) {
    this.iataAirlinesCodeFactory = iataAirlinesCodeFactory;
  }

  public FlightId flightId(String rawFlightId) {
    final FlightId flightId = FlightId.fromRaw(rawFlightId);
    var flightIdParts = rawFlightId.split(" ");
    var rawAirlinesCode = flightIdParts[0];
    validateAirlinesIATACode(rawAirlinesCode);
    return flightId;
  }

  private void validateAirlinesIATACode(String rawAirlinesCode) {
    iataAirlinesCodeFactory.code(rawAirlinesCode);
  }

}

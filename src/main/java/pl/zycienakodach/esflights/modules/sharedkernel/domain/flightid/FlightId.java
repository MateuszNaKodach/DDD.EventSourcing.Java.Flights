package pl.zycienakodach.esflights.modules.sharedkernel.domain.flightid;

import pl.zycienakodach.esflights.modules.sharedkernel.domain.iata.IATAAirlinesCode;

public record FlightId(IATAAirlinesCode airlines, FlightNumber number, FlightPostfix postfix) {

  public static FlightId fromRaw(String raw) {
    var idParts = raw.split(" ");
    if (idParts.length != 3) {
      throw new IllegalArgumentException("Invalid flightId " + raw);
    }

    try {
      return new FlightId(
          IATAAirlinesCode.fromRaw(idParts[0]),
          FlightNumber.fromRaw(idParts[1]),
          FlightPostfix.fromRaw(idParts[2])
      );
    } catch (Exception e) {
      throw new IllegalArgumentException("Invalid flightId " + raw, e);
    }
  }

  public String raw() {
    return this.airlines.raw() + " " + this.number.raw() + " " + this.postfix.raw();
  }
}

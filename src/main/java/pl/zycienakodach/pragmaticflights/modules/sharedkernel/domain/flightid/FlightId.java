package pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.flightid;

import pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.iata.IATAAirlinesCode;

public record FlightId(IATAAirlinesCode airlines, FlightNumber number, FlightPostfix postfix) {

  public static FlightId fromRaw(String raw) {
    var idParts = raw.split(" ");

    return new FlightId(
        IATAAirlinesCode.fromRaw(idParts[0]),
        FlightNumber.fromRaw(idParts[1]),
        FlightPostfix.fromRaw(idParts[2])
    );
  }

  public String raw() {
    return this.airlines.raw() + " " + this.number.raw() + " " + this.postfix.raw();
  }
}

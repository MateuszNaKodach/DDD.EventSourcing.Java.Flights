package pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.flightid;

import pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.iata.IATAAirlinesCode;

public record FlightId(IATAAirlinesCode airlines, FlightNumber number, FlightPostfix postfix) {

  public String raw() {
    return this.airlines.raw() + " " + this.number.raw() + " " + this.postfix.raw();
  }
}

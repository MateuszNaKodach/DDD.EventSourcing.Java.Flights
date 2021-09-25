package pl.zycienakodach.pragmaticflights.sharedkernel.domain.flightid;

import pl.zycienakodach.pragmaticflights.sharedkernel.domain.iata.IATAAirlinesCode;

public record FlightId(IATAAirlinesCode airlines, FlightNumber number, FlightPostfix postfix) {

  public String raw() {
    return this.airlines.raw() + " " + this.number.raw() + " " + this.postfix.raw();
  }
}

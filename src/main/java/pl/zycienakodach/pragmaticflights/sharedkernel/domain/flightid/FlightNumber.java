package pl.zycienakodach.pragmaticflights.sharedkernel.domain.flightid;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public record FlightNumber(int d1, int d2, int d3, int d4, int d5) {

  public String raw() {
    return Stream.of(d1,d2,d3,d4,d5)
        .map(Object::toString)
        .collect(Collectors.joining());
  }
}

package pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.flightid;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public record FlightPostfix(char l1, char l2, char l3) {

  public static FlightPostfix fromRaw(String raw) {
    return new FlightPostfix(
        raw.charAt(0),
        raw.charAt(1),
        raw.charAt(2)
    );
  }

  public String raw() {
    return Stream.of(l1, l2, l3)
        .map(Object::toString)
        .collect(Collectors.joining());
  }
}

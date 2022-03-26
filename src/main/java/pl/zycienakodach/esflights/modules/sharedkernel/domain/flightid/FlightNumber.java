package pl.zycienakodach.esflights.modules.sharedkernel.domain.flightid;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public record FlightNumber(int d1, int d2, int d3, int d4, int d5) {

  public static FlightNumber fromRaw(String raw){
    var numberParts = Arrays.stream(raw.split("")).mapToInt(Integer::parseInt).toArray();

    return new FlightNumber(
        numberParts[0],
        numberParts[1],
        numberParts[2],
        numberParts[3],
        numberParts[4]
    );
  }

  public String raw() {
    return Stream.of(d1,d2,d3,d4,d5)
        .map(Object::toString)
        .collect(Collectors.joining());
  }
}

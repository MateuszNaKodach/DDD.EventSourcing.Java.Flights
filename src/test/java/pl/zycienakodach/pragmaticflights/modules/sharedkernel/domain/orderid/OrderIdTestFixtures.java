package pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.orderid;

import java.util.UUID;

public class OrderIdTestFixtures {

  public static String rawOrderId() {
    return anOrderId().raw();
  }

  public static String rawOrderId(String customerId, String flightCourseId) {
    return OrderId.fromRaw(customerId + " & " + flightCourseId).raw();
  }


  public static String rawOrderId(String flightCourseId) {
    var rawCustomerId = UUID.randomUUID().toString();
    return OrderId.fromRaw(rawCustomerId + " & " + flightCourseId).raw();
  }

  public static OrderId anOrderId() {
    var rawCustomerId = UUID.randomUUID().toString();
    return OrderId.fromRaw(rawCustomerId + " & KLM 12345 ABC + 2021-10-03T00:00:00.000000Z");
  }

}

package pl.zycienakodach.esflights.modules.sharedkernel.domain.orderid;

import pl.zycienakodach.esflights.modules.sharedkernel.domain.customerid.CustomerId;
import pl.zycienakodach.esflights.modules.sharedkernel.domain.flightid.FlightCourseId;

import java.util.Objects;

public final class OrderId {
  private final CustomerId customerId;
  private final FlightCourseId flightCourseId;

  private OrderId(CustomerId customerId, FlightCourseId flightCourseId) {
    this.customerId = customerId;
    this.flightCourseId = flightCourseId;
  }

  public static OrderId of(CustomerId customerId, FlightCourseId flightCourseId) {
    return new OrderId(customerId, flightCourseId);
  }

  public static OrderId fromRaw(String raw) {
    var orderIdParts = raw.split(" & ");
    return new OrderId(
        CustomerId.fromRaw(orderIdParts[0]),
        FlightCourseId.fromRaw(orderIdParts[1])
    );
  }

  public CustomerId customerId() {
    return customerId;
  }

  public FlightCourseId flightCourseId() {
    return flightCourseId;
  }

  public String raw() {
    return customerId.raw() + " & " + flightCourseId.raw();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof OrderId orderId)) return false;
    return Objects.equals(customerId, orderId.customerId) && Objects.equals(flightCourseId, orderId.flightCourseId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(customerId, flightCourseId);
  }
}

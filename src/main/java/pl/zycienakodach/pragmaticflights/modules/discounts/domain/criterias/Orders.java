package pl.zycienakodach.pragmaticflights.modules.discounts.domain.criterias;

import pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.orderid.OrderId;

import java.util.Optional;

public interface Orders {

  Optional<FlightOrder> findByOrderId(OrderId orderId);

}

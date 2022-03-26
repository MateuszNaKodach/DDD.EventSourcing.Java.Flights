package pl.zycienakodach.esflights.modules.discounts.domain.criterias;

import pl.zycienakodach.esflights.modules.sharedkernel.domain.orderid.OrderId;

import java.util.Optional;

public interface Orders {

  Optional<FlightOrder> findByOrderId(OrderId orderId);

}

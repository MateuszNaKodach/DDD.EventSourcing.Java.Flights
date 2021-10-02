package pl.zycienakodach.pragmaticflights.modules.discounts.infrastructure.flightorders;

import pl.zycienakodach.pragmaticflights.modules.discounts.domain.criterias.FlightOrder;
import pl.zycienakodach.pragmaticflights.modules.discounts.domain.criterias.Orders;
import pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.customerid.CustomerId;
import pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.flightid.FlightId;
import pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.iata.IATAAirportCode;
import pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.orderid.OrderId;
import pl.zycienakodach.pragmaticflights.readmodels.flightorders.api.FlightOrdersRepository;

import java.util.Optional;

public class FlightOrdersReadModelAdapter implements Orders {

  private final FlightOrdersRepository flightOrdersRepository;

  public FlightOrdersReadModelAdapter(FlightOrdersRepository flightOrdersRepository) {
    this.flightOrdersRepository = flightOrdersRepository;
  }

  @Override
  public Optional<FlightOrder> findByOrderId(OrderId orderId) {
    return flightOrdersRepository.findBy(orderId.raw())
        .map(o -> new FlightOrder(
            OrderId.fromRaw(o.orderId()),
            new CustomerId(o.customerId()),
            o.flightDate(),
            new FlightOrder.Flight(
                FlightId.fromRaw(o.flight().flightId()),
                IATAAirportCode.fromRaw(o.flight().origin()),
                IATAAirportCode.fromRaw(o.flight().destination()),
                o.flight().departureTime()
            )
        ));
  }
}

package pl.zycienakodach.pragmaticflights.modules.ordering;

import pl.zycienakodach.pragmaticflights.modules.ordering.api.commands.OfferFlightCourseForSell;
import pl.zycienakodach.pragmaticflights.modules.ordering.api.commands.SubmitFlightOrder;
import pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.customerid.CustomerId;
import pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.flightid.FlightCourseId;
import pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.iata.IATAAirportCode;
import pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.orderid.OrderId;
import pl.zycienakodach.pragmaticflights.sdk.Application;
import pl.zycienakodach.pragmaticflights.sdk.ApplicationModule;
import pl.zycienakodach.pragmaticflights.sdk.application.eventstream.EventStreamName;
import pl.zycienakodach.pragmaticflights.sdk.application.time.TimeProvider;

import static pl.zycienakodach.pragmaticflights.modules.ordering.domain.Offering.offerForSell;
import static pl.zycienakodach.pragmaticflights.modules.ordering.domain.Ordering.submitFlightCourseOrder;
import static pl.zycienakodach.pragmaticflights.sdk.application.eventstream.EventStreamName.category;
import static pl.zycienakodach.pragmaticflights.sdk.application.eventstream.EventStreamName.streamId;

public class OrderingModule implements ApplicationModule {

  private final TimeProvider timeProvider;

  public OrderingModule(TimeProvider timeProvider) {
    this.timeProvider = timeProvider;
  }

  @Override
  public ApplicationModule configure(Application app) {
    app
        .onCommand(OfferFlightCourseForSell.class,
            (c, m) -> new EventStreamName(category("FlightCourseSells"), streamId(c.flightCourseId())),
            (c) -> offerForSell(
                FlightCourseId.fromRaw(c.flightCourseId()),
                IATAAirportCode.fromRaw(c.origin()),
                IATAAirportCode.fromRaw(c.destination())
            ))
        .onCommand(SubmitFlightOrder.class,
            (c, m) -> new EventStreamName(category("FlightCourseSells"), streamId(c.flightCourseId())),
            (c) -> {
              final var customerId = new CustomerId(c.customerId());
              final var flightCourseId = FlightCourseId.fromRaw(c.flightCourseId());
              return submitFlightCourseOrder(
                  OrderId.of(customerId,flightCourseId),
                  customerId,
                  flightCourseId,
                  timeProvider.get()
              );
            }
        );
    return this;
  }
}

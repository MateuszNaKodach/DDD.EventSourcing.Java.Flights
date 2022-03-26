package pl.zycienakodach.esflights.modules.ordering;

import pl.zycienakodach.esflights.modules.ordering.api.commands.OfferFlightCourseForSell;
import pl.zycienakodach.esflights.modules.ordering.api.commands.SubmitFlightOrder;
import pl.zycienakodach.esflights.modules.sharedkernel.domain.customerid.CustomerId;
import pl.zycienakodach.esflights.modules.sharedkernel.domain.flightid.FlightCourseId;
import pl.zycienakodach.esflights.modules.sharedkernel.domain.iata.IATAAirportCode;
import pl.zycienakodach.esflights.modules.sharedkernel.domain.orderid.OrderId;
import pl.zycienakodach.esflights.sdk.Application;
import pl.zycienakodach.esflights.sdk.ApplicationModule;
import pl.zycienakodach.esflights.sdk.application.eventstream.EventStreamName;
import pl.zycienakodach.esflights.sdk.application.time.TimeProvider;

import static pl.zycienakodach.esflights.modules.ordering.domain.Offering.offerForSell;
import static pl.zycienakodach.esflights.modules.ordering.domain.Ordering.submitFlightCourseOrder;

public class OrderingModule implements ApplicationModule {

  private final TimeProvider timeProvider;

  public OrderingModule(TimeProvider timeProvider) {
    this.timeProvider = timeProvider;
  }

  @Override
  public ApplicationModule configure(Application app) {
    app
        .onCommand(OfferFlightCourseForSell.class,
            (c, m) -> EventStreamName.ofCategory("FlightCourseSells").withId(c.flightCourseId()),
            (c) -> offerForSell(
                FlightCourseId.fromRaw(c.flightCourseId()),
                IATAAirportCode.fromRaw(c.origin()),
                IATAAirportCode.fromRaw(c.destination())
            ))
        .onCommand(SubmitFlightOrder.class,
            (c, m) -> EventStreamName.ofCategory("FlightCourseSells").withId(c.flightCourseId()),
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

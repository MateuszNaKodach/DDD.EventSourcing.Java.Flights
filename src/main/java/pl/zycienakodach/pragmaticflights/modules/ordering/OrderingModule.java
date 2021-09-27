package pl.zycienakodach.pragmaticflights.modules.ordering;

import pl.zycienakodach.pragmaticflights.modules.ordering.api.commands.OfferFlightForSell;
import pl.zycienakodach.pragmaticflights.modules.ordering.api.commands.SubmitFlightOrder;
import pl.zycienakodach.pragmaticflights.modules.ordering.application.FlightsOffers;
import pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.customerid.CustomerId;
import pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.flightid.FlightId;
import pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.iata.IATAAirportCode;
import pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.orderid.OrderId;
import pl.zycienakodach.pragmaticflights.sdk.Application;
import pl.zycienakodach.pragmaticflights.sdk.ApplicationModule;
import pl.zycienakodach.pragmaticflights.sdk.application.EventStreamName;
import pl.zycienakodach.pragmaticflights.sdk.application.time.TimeProvider;

import static pl.zycienakodach.pragmaticflights.modules.ordering.domain.Offering.offerForSell;
import static pl.zycienakodach.pragmaticflights.modules.ordering.domain.Ordering.submitFlightOrder;
import static pl.zycienakodach.pragmaticflights.sdk.application.EventStreamName.category;
import static pl.zycienakodach.pragmaticflights.sdk.application.EventStreamName.streamId;

public class OrderingModule implements ApplicationModule {

  private final FlightsOffers flightsOffers;
  private final TimeProvider timeProvider;

  public OrderingModule(FlightsOffers flightsOffers, TimeProvider timeProvider) {
    this.flightsOffers = flightsOffers;
    this.timeProvider = timeProvider;
  }

  @Override
  public ApplicationModule configure(Application app) {
    app
        .onCommand(OfferFlightForSell.class,
            (c, m) -> new EventStreamName(category(m.tenantId().raw(), "FlightSellOffer"), streamId(c.flightId())),
            (c) -> offerForSell(
                FlightId.fromRaw(c.flightId()),
                IATAAirportCode.fromRaw(c.origin()),
                IATAAirportCode.fromRaw(c.destination()),
                c.departureTime(),
                c.departureDays()
            ))
        .onCommand(SubmitFlightOrder.class,
            (c, m) -> new EventStreamName(category(m.tenantId().raw(), "FlightOrder"), streamId(c.orderId())),
            (c) -> {
              var offer = flightsOffers.findBy(FlightId.fromRaw(c.flightId()));
              if (offer.isEmpty()) {
                throw new IllegalStateException("Offer not exist!");
              }
              return submitFlightOrder(
                  new OrderId(c.orderId()),
                  new CustomerId(c.customerId()),
                  offer.get(),
                  c.flightDate(),
                  timeProvider.get()
              );
            }
        );
    return this;
  }
}

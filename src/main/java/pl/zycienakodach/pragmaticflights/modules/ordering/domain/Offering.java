package pl.zycienakodach.pragmaticflights.modules.ordering.domain;

import pl.zycienakodach.pragmaticflights.modules.ordering.api.events.FlightCourseOfferedForSell;
import pl.zycienakodach.pragmaticflights.modules.ordering.api.events.OrderingEvents;
import pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.flightid.FlightCourseId;
import pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.iata.IATAAirportCode;
import pl.zycienakodach.pragmaticflights.sdk.domain.DomainLogic;

import java.util.List;

public class Offering {

  public static DomainLogic<OrderingEvents> offerForSell(
      FlightCourseId flightCourseId,
      IATAAirportCode origin,
      IATAAirportCode destination
  ) {
    return (List<OrderingEvents> pastEvents) -> {
      var flightOfferedForSell = new FlightCourseOfferedForSell(
          flightCourseId.raw(),
          origin.raw(),
          destination.raw()
      );
      return List.of(flightOfferedForSell);
    };
  }
}

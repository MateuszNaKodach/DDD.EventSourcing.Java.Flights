package pl.zycienakodach.esflights.modules.ordering.domain;

import pl.zycienakodach.esflights.modules.ordering.api.events.FlightCourseOfferedForSell;
import pl.zycienakodach.esflights.modules.ordering.api.events.OrderingEvents;
import pl.zycienakodach.esflights.modules.sharedkernel.domain.flightid.FlightCourseId;
import pl.zycienakodach.esflights.modules.sharedkernel.domain.iata.IATAAirportCode;
import pl.zycienakodach.esflights.sdk.domain.DomainLogic;

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

package pl.zycienakodach.pragmaticflights.modules.ordering.domain;

import java.time.LocalDate;

record OrderItem(FlightOffer offer, LocalDate departureDate) {

  OrderItem {
    var departureOnSelectedDay = offer.departureDays().stream().anyMatch(d -> d.equals(departureDate.getDayOfWeek()));
    if (!departureOnSelectedDay) {
      throw new RuntimeException("Cannot order flight ticket for day when the flight does not departures!");
    }
  }

}

package pl.zycienakodach.pragmaticflights.modules.ordering.domain;

import pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.flightid.FlightId;

interface FreeSeatsPolicy {

  boolean hasFreeSeat(FlightId flightId);

}

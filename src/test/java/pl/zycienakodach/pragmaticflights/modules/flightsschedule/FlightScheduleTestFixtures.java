package pl.zycienakodach.pragmaticflights.modules.flightsschedule;

import pl.zycienakodach.pragmaticflights.modules.flightsschedule.api.events.FlightScheduled;

import java.time.Instant;
import java.util.List;
import java.util.function.Function;

class FlightScheduleTestFixtures {

  public static List<FlightScheduled> scheduledOnDates(List<String> dates, Function<Instant, FlightScheduled> eventAtDate) {
    return scheduledOnInstants(dates.stream().map(Instant::parse).toList(), eventAtDate);
  }

  public static List<FlightScheduled> scheduledOnInstants(List<Instant> dates, Function<Instant, FlightScheduled> eventAtDate) {
    return dates.stream().map(eventAtDate)
        .toList();
  }
}

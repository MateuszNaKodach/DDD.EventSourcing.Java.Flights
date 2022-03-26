package pl.zycienakodach.esflights.modules.flightsschedule;

import pl.zycienakodach.esflights.modules.flightsschedule.api.events.FlightCourseScheduled;

import java.time.Instant;
import java.util.List;
import java.util.function.Function;

class FlightScheduleTestFixtures {

  public static List<FlightCourseScheduled> scheduledOnDates(List<String> dates, Function<Instant, FlightCourseScheduled> eventAtDate) {
    return scheduledOnInstants(dates.stream().map(Instant::parse).toList(), eventAtDate);
  }

  public static List<FlightCourseScheduled> scheduledOnInstants(List<Instant> dates, Function<Instant, FlightCourseScheduled> eventAtDate) {
    return dates.stream().map(eventAtDate)
        .toList();
  }
}

package pl.zycienakodach.pragmaticflights.sdk.domain;

import java.time.LocalDate;
import java.util.List;

public record LocalDateRange(LocalDate fromDate, LocalDate toDate) {

  public LocalDateRange {
    if (toDate.isBefore(fromDate)) {
      throw new IllegalArgumentException("LocalDateRange toDate cannot be before fromDate!");
    }
  }

  public static LocalDateRange future(LocalDate currentDate, LocalDate fromDate, LocalDate toDate) {
    if (fromDate.isBefore(currentDate) || toDate.isBefore(currentDate)) {
      throw new IllegalArgumentException("The range must be in the future!");
    }
    return new LocalDateRange(fromDate, toDate);
  }

  public List<LocalDate> datesInRange() {
    return fromDate.datesUntil(toDate.plusDays(1))
        .toList();
  }
}

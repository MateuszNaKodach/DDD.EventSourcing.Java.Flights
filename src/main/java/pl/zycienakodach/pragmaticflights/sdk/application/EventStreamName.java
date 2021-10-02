package pl.zycienakodach.pragmaticflights.sdk.application;

import java.util.stream.Stream;

public record EventStreamName(String category, String id) {

  @Override
  public String toString() {
    return category + "_" + id;
  }

  public EventStreamName withCategoryPrefix(String prefix) {
    return new EventStreamName(category(prefix, category), id);
  }

  public static String category(String... categoryParts) {
    return Stream.of(categoryParts)
        .reduce("", (p1, p2) -> p1 + "-" + p2);
  }

  public static String streamId(String... idParts) {
    return Stream.of(idParts)
        .reduce("", (p1, p2) -> p1 + "-" + p2);
  }
}

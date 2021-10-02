package pl.zycienakodach.pragmaticflights.sdk.application.eventstream;

import java.util.Objects;
import java.util.stream.Stream;

public final class EventStreamName {
  private final String category;
  private final String id;

  public EventStreamName(String category, String id) {
    this.category = category;
    this.id = id;
  }

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

  public String category() {
    return category;
  }

  public String id() {
    return id;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) return true;
    if (obj == null || obj.getClass() != this.getClass()) return false;
    var that = (EventStreamName) obj;
    return Objects.equals(this.category, that.category) &&
        Objects.equals(this.id, that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(category, id);
  }

}

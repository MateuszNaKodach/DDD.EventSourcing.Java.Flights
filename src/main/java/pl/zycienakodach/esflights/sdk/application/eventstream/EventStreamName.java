package pl.zycienakodach.esflights.sdk.application.eventstream;

import java.util.Objects;

public final class EventStreamName {
  private final String category;
  private final String id;

  private EventStreamName(String category, String id) {
    this.category = category;
    this.id = id;
  }

  public static Builder ofCategory(String category) {
    return new Builder(category);
  }

  @Override
  public String toString() {
    return category + "_" + id;
  }

  public EventStreamName withCategoryPrefix(String prefix) {
    return new EventStreamName(prefix + "-" + category, id);
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


  public static class Builder {
    private final String category;

    private Builder(String category) {
      this.category = category;
    }

    public EventStreamName withId(String id) {
      return new EventStreamName(category, id);
    }
  }

}

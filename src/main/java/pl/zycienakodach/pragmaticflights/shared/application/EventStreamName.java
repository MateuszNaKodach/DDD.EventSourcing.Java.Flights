package pl.zycienakodach.pragmaticflights.shared.application;

public record EventStreamName(String category, String id) {

  @Override
  public String toString() {
    return category + "_" + id;
  }
}

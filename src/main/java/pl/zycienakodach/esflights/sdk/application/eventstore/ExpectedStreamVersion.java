package pl.zycienakodach.esflights.sdk.application.eventstore;

sealed public class ExpectedStreamVersion permits ExpectedStreamVersion.Any, ExpectedStreamVersion.NotExists, ExpectedStreamVersion.Exactly {
  public static final class Any extends ExpectedStreamVersion {
    @Override
    public String toString() {
      return "Any";
    }
  }

  public static final class NotExists extends ExpectedStreamVersion {
    @Override
    public String toString() {
      return "NotExists";
    }
  }

  public static final class Exactly extends ExpectedStreamVersion {
    private final int raw;

    public Exactly(int raw) {
      this.raw = raw;
    }

    public int raw() {
      return raw;
    }

    @Override
    public String toString() {
      return "Exactly " + raw;
    }
  }
}

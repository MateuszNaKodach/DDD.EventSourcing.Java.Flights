package pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.iata;

import java.util.Objects;

public final class IATAAirlinesCode {
  private final String raw;

  IATAAirlinesCode(String raw) {
    this.raw = raw;
  }

  public String raw() {
    return raw;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) return true;
    if (obj == null || obj.getClass() != this.getClass()) return false;
    var that = (IATAAirlinesCode) obj;
    return Objects.equals(this.raw, that.raw);
  }

  @Override
  public int hashCode() {
    return Objects.hash(raw);
  }

  @Override
  public String toString() {
    return "IATAAirlinesCode[" +
        "raw=" + raw + ']';
  }

}

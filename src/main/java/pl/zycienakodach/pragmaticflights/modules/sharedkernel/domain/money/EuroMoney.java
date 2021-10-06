package pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.money;

import java.math.BigDecimal;
import java.util.Objects;

public final class EuroMoney {
  private final BigDecimal value;

  private EuroMoney(BigDecimal value) {
    this.value = value;
  }

  public static EuroMoney of(BigDecimal value) {
    return new EuroMoney(value);
  }

  public static EuroMoney of(String value) {
    return new EuroMoney(new BigDecimal(value));
  }

  public static EuroMoney of(Integer value) {
    return new EuroMoney(new BigDecimal(value));
  }

  public static EuroMoney of(double value) {
    return new EuroMoney(new BigDecimal(value));
  }

  public static EuroMoney zero() {
    return new EuroMoney(BigDecimal.ZERO);
  }

  public EuroMoney add(EuroMoney euroMoney) {
    return new EuroMoney(this.value.add(euroMoney.value));
  }

  public EuroMoney subtract(EuroMoney euroMoney) {
    return new EuroMoney(this.value.subtract(euroMoney.value));
  }

  public boolean greaterOrEqual(EuroMoney euroMoney) {
    return this.value.compareTo(euroMoney.value) >= 0;
  }

  public BigDecimal toBigDecimal() {
    return value;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) return true;
    if (obj == null || obj.getClass() != this.getClass()) return false;
    var that = (EuroMoney) obj;
    return Objects.equals(this.value, that.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(value);
  }

  @Override
  public String toString() {
    return this.value.toString();
  }

}

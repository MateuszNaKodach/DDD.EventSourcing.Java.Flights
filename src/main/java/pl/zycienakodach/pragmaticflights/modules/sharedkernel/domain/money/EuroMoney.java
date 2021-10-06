package pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.money;

// fixme: big decimal? operations on integers? What with minus?
public record EuroMoney(double value) {

  public static EuroMoney of(double value) {
    return new EuroMoney(value);
  }

  public static EuroMoney of(Integer value) {
    return new EuroMoney(value.doubleValue());
  }

  public static EuroMoney zero() {
    return new EuroMoney(0);
  }

  public EuroMoney plus(EuroMoney euroMoney) {
    return new EuroMoney(this.value + euroMoney.value);
  }

  public EuroMoney minus(EuroMoney euroMoney) {
    return new EuroMoney(this.value - euroMoney.value);
  }

  public boolean greaterOrEqual(EuroMoney euroMoney) {
    return this.value >= euroMoney.value;
  }

  public Double toDouble() {
    return this.value;
  }
}

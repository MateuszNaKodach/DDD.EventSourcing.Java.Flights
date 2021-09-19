package pl.zycienakodach.pragmaticflights.shared.application;

sealed class ExpectedStreamVersion permits ExpectedStreamVersion.Any, ExpectedStreamVersion.NotExists, ExpectedStreamVersion.Exactly {
  static final class Any extends ExpectedStreamVersion {

  }

  static final class NotExists extends ExpectedStreamVersion {

  }


  static final class Exactly extends ExpectedStreamVersion {
    private final int raw;

    Exactly(int raw) {
      this.raw = raw;
    }

    int raw() {
      return raw;
    }
  }
}

package pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.iata;

import java.util.function.Predicate;

public class IATAAirportCodeFactory {

  private final Predicate<String> rawCodeValidator;

  public IATAAirportCodeFactory(Predicate<String> rawCodeValidator) {
    this.rawCodeValidator = rawCodeValidator;
  }

  public IATAAirportCode code(String raw){
    if(!rawCodeValidator.test(raw)){
      throw new IllegalArgumentException(raw + " is not valid IATA Airport code!");
    }
    return new IATAAirportCode(raw);
  }

}

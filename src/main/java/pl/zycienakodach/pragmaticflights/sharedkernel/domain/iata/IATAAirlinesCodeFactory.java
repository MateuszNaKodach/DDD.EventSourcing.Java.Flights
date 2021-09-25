package pl.zycienakodach.pragmaticflights.sharedkernel.domain.iata;

import java.util.function.Predicate;

public class IATAAirlinesCodeFactory {

  private final Predicate<String> rawCodeValidator;

  public IATAAirlinesCodeFactory(Predicate<String> rawCodeValidator) {
    this.rawCodeValidator = rawCodeValidator;
  }

  public IATAAirlinesCode code(String raw){
    if(!rawCodeValidator.test(raw)){
      throw new IllegalArgumentException(raw + " is not valid IATA Airlines code!");
    }
    return new IATAAirlinesCode(raw);
  }

}

package pl.zycienakodach.esflights.modules.sharedkernel.domain.customerid;

public record CustomerId(String raw) {

  public static CustomerId fromRaw(String raw){
    return new CustomerId(raw);
  }
}

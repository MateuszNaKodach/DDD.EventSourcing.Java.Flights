package pl.zycienakodach.pragmaticflights.modules.sharedkernel.domain.customerid;

public record CustomerId(String raw) {

  public static CustomerId fromRaw(String raw){
    return new CustomerId(raw);
  }
}

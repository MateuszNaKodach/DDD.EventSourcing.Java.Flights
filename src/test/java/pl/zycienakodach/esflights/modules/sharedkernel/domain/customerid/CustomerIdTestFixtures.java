package pl.zycienakodach.esflights.modules.sharedkernel.domain.customerid;


import java.util.UUID;

public class CustomerIdTestFixtures {

  public static CustomerId aCustomerId() {
    return new CustomerId(UUID.randomUUID().toString());
  }

  public static String rawCustomerId() {
    return aCustomerId().raw();
  }

}

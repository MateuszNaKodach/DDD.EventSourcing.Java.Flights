package pl.zycienakodach.pragmaticflights.sdk.application.tenant;

public record TenantId(String raw) {

  public static TenantId undefined(){
    return new TenantId("UNDEFINED");
  }

}

package pl.zycienakodach.pragmaticflights.sdk.application.tenant;

public record TenantId(String raw) {

  private static final TenantId UNDEFINED = new TenantId("UNDEFINED");

  public static TenantId undefined() {
    return UNDEFINED;
  }

}

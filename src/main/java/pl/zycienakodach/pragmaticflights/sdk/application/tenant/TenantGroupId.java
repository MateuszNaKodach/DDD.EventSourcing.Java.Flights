package pl.zycienakodach.pragmaticflights.sdk.application.tenant;

public record TenantGroupId(String raw) {

  private static final TenantGroupId UNDEFINED = new TenantGroupId("UNDEFINED");

  public static TenantGroupId of(String raw) {
    return new TenantGroupId(raw);
  }

  public static TenantGroupId undefined() {
    return UNDEFINED;
  }
}

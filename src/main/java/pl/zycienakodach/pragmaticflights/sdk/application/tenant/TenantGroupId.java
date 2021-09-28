package pl.zycienakodach.pragmaticflights.sdk.application.tenant;

public record TenantGroupId(String raw) {

  public static TenantGroupId of(String raw) {
    return new TenantGroupId(raw);
  }
}

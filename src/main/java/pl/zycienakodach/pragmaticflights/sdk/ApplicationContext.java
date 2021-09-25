package pl.zycienakodach.pragmaticflights.sdk;

import pl.zycienakodach.pragmaticflights.sdk.application.tenant.TenantId;

public interface ApplicationContext {
  TenantId tenantId();
}

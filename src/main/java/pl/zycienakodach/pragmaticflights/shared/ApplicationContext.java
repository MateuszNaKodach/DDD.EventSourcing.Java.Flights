package pl.zycienakodach.pragmaticflights.shared;

import pl.zycienakodach.pragmaticflights.shared.application.tenant.TenantId;

public interface ApplicationContext {
  TenantId tenantId();
}

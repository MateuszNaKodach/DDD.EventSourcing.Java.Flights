package pl.zycienakodach.pragmaticflights.shared.infrastructure.tenant;

import pl.zycienakodach.pragmaticflights.shared.application.tenant.TenantGroupId;
import pl.zycienakodach.pragmaticflights.shared.application.tenant.TenantGroups;
import pl.zycienakodach.pragmaticflights.shared.application.tenant.TenantId;
import java.util.Map;

class InMemoryTenantRegistry implements TenantGroups {

  private final Map<TenantId, TenantGroupId> tenantGroup;

  InMemoryTenantRegistry(Map<TenantId, TenantGroupId> tenantGroup) {
    this.tenantGroup = tenantGroup;
  }

  @Override
  public TenantGroupId tenantGroupOf(TenantId tenantId) {
    return this.tenantGroup.get(tenantId);
  }
}

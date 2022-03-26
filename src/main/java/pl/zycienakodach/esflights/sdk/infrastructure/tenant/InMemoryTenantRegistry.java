package pl.zycienakodach.esflights.sdk.infrastructure.tenant;

import pl.zycienakodach.esflights.sdk.application.tenant.TenantGroupId;
import pl.zycienakodach.esflights.sdk.application.tenant.TenantGroups;
import pl.zycienakodach.esflights.sdk.application.tenant.TenantId;

import java.util.Map;

public class InMemoryTenantRegistry implements TenantGroups {

  private final Map<TenantId, TenantGroupId> tenantGroup;

  public InMemoryTenantRegistry(Map<TenantId, TenantGroupId> tenantGroup) {
    this.tenantGroup = tenantGroup;
  }

  @Override
  public TenantGroupId tenantGroupOf(TenantId tenantId) {
    return this.tenantGroup.getOrDefault(tenantId, TenantGroupId.undefined());
  }
}

package pl.zycienakodach.pragmaticflights.shared.application.tenant;

import java.util.function.Supplier;

public interface TenantFilter {

  <R> R onlyForTenantInGroup(String tenantGroupId, Supplier<R> function);

}

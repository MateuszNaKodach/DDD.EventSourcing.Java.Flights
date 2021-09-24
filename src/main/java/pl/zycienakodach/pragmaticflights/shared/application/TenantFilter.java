package pl.zycienakodach.pragmaticflights.shared.application;

import java.util.function.Supplier;

public interface TenantFilter {

  <R> R onlyForTenantInGroup(String tenantGroupId, Supplier<R> function);

}

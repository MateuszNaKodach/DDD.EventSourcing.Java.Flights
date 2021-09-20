package pl.zycienakodach.pragmaticflights.pricing;

import pl.zycienakodach.pragmaticflights.shared.ModuleConfiguration;
import pl.zycienakodach.pragmaticflights.shared.application.ApplicationService;

public class PricingModule {

  //tenant here? / wrapper like tenantFeature? / inject context for commands / eventHandlers - how? / execution context
  private final ApplicationService applicationService;

  public PricingModule(ApplicationService applicationService) {
    this.applicationService = applicationService;
  }

  public PricingModule configure(ModuleConfiguration module){
    return this;
  }

}

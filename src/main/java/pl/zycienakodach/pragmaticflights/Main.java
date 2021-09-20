package pl.zycienakodach.pragmaticflights;

import pl.zycienakodach.pragmaticflights.pricing.PricingModule;
import pl.zycienakodach.pragmaticflights.shared.ModuleConfiguration;
import pl.zycienakodach.pragmaticflights.shared.application.ApplicationService;
import pl.zycienakodach.pragmaticflights.shared.infrastructure.InMemoryCommandBus;
import pl.zycienakodach.pragmaticflights.shared.infrastructure.InMemoryEventBus;
import pl.zycienakodach.pragmaticflights.shared.infrastructure.InMemoryEventStore;

class Main {

  public static void main(String[] args) {
    var eventStore = new InMemoryEventStore(new InMemoryEventBus());

    var commandBus = new InMemoryCommandBus();
    var moduleConfiguration = new ModuleConfiguration(commandBus, eventStore);

    var applicationService = new ApplicationService(eventStore);
    var pricingModule = new PricingModule(applicationService).configure(moduleConfiguration);
  }

}

//record IATAAirlinesCode(String raw){
//}
//
//record IATAAirportCode(String raw){
//}
//
//class Tenant {
//
//}
//
//class FlightId {
//
//}
//
//record Flight(FlightId id) {
//}
//
//
//class Price {
//
//}
//
//class Pricing {
//  static calculatePrice()
//}
//
//class DiscountsRepository {
//
//}
//
//class DiscountApplicability {
//  boolean applicableFor(Flight flight);
//}
//discounts-registry
// Tenant configuration
//Tenant in stream id
//FeaturesToggles.isEnabled(tenan

record TenantId(String raw){}



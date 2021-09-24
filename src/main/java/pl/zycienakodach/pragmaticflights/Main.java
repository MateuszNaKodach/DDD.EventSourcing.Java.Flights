package pl.zycienakodach.pragmaticflights;

import pl.zycienakodach.pragmaticflights.pricing.PricingModule;
import pl.zycienakodach.pragmaticflights.shared.Application;

import static pl.zycienakodach.pragmaticflights.ApplicationTestFixtures.inMemoryApplication;

class Main {

  public static void main(String[] args) {
    Application app = inMemoryApplication();
    var pricingModule = new PricingModule().configure(app);
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



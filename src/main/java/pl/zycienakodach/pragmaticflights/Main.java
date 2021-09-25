package pl.zycienakodach.pragmaticflights;

import pl.zycienakodach.pragmaticflights.flightsschedule.FlightsScheduleModule;
import pl.zycienakodach.pragmaticflights.ordering.OrderingModule;
import pl.zycienakodach.pragmaticflights.pricing.PricingModule;

import static pl.zycienakodach.pragmaticflights.ApplicationTestFixtures.inMemoryApplication;

class Main {

  public static void main(String[] args) {
    inMemoryApplication()
        .withModule(new PricingModule())
        .withModule(new OrderingModule());
        //.withModule(new FlightsScheduleModule());
  }

}

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



package pl.zycienakodach.pragmaticflights;

class Main {

  public static void main(String[] args) {
    System.out.println("Main");
  }

}

record IATAAirlinesCode(String raw){
}

record IATAAirportCode(String raw){
}

class Tenant {

}

class FlightId {

}

record Flight(FlightId id) {
}


class Price {

}

class Pricing {
  static calculatePrice()
}

class DiscountsRepository {

}

class DiscountApplicability {
  boolean applicableFor(Flight flight);
}



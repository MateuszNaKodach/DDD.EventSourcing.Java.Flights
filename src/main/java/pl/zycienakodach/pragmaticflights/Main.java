package pl.zycienakodach.pragmaticflights;

import static pl.zycienakodach.pragmaticflights.ApplicationTestFixtures.inMemoryApplication;
import static pl.zycienakodach.pragmaticflights.ApplicationTestFixtures.withAllModules;

class Main {

  public static void main(String[] args) {
    withAllModules(inMemoryApplication());
  }

}

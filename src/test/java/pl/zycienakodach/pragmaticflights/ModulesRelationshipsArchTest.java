package pl.zycienakodach.pragmaticflights;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import com.tngtech.archunit.library.Architectures;

@AnalyzeClasses(packages = "pl.zycienakodach.pragmaticflights.modules", importOptions = ImportOption.DoNotIncludeTests.class)
class ModulesRelationshipsArchTest {

  private static final String FLIGHTS_SCHEDULE_MODULE = "..flightsschedule..";
  private static final String DISCOUNTS_MODULE = "..discounts..";
  private static final String ORDERING_MODULE = "..ordering..";
  private static final String PRICING_MODULE = "..pricing..";

  @ArchTest
  public static final ArchRule flightsScheduleModuleNoDependenciesToOthers =
      ArchRuleDefinition.noClasses()
          .that()
          .resideInAPackage(FLIGHTS_SCHEDULE_MODULE)
          .should()
          .dependOnClassesThat()
          .resideInAnyPackage(DISCOUNTS_MODULE, ORDERING_MODULE, PRICING_MODULE)
          .because("Modules should be totally independent from each other.");
}

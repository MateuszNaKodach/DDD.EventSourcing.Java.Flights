package pl.zycienakodach.pragmaticflights;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.library.Architectures;

@AnalyzeClasses(packages = "pl.zycienakodach.pragmaticflights.modules", importOptions = ImportOption.DoNotIncludeTests.class)
class ModuleLayersArchTest {

  private static final String DOMAIN_LAYER = "Domain";
  private static final String APPLICATION_LAYER = "Application";
  private static final String INFRASTRUCTURE_LAYER = "Infrastructure";
  private static final String MODULE_API_LAYER = "Module API";

//  @ArchTest
//  public static final ArchRule layeredArchitecture = Architectures
//      .layeredArchitecture()
//      .layer(DOMAIN_LAYER).definedBy("..domain..")
//      .layer(APPLICATION_LAYER).definedBy("..application..")
//      .layer(MODULE_API_LAYER).definedBy("..api..")
//      .layer(INFRASTRUCTURE_LAYER).definedBy("..infrastructure..")
//      .whereLayer(DOMAIN_LAYER).mayOnlyBeAccessedByLayers(MODULE_API_LAYER, APPLICATION_LAYER, INFRASTRUCTURE_LAYER)
//      .whereLayer(APPLICATION_LAYER).mayOnlyBeAccessedByLayers(MODULE_API_LAYER, INFRASTRUCTURE_LAYER)
//      .whereLayer(INFRASTRUCTURE_LAYER).mayNotBeAccessedByAnyLayer();

}

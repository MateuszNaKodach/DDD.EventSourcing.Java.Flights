package pl.zycienakodach.pragmaticflights.sdk.application;

import static pl.zycienakodach.pragmaticflights.sdk.application.EventStreamName.category;
import static pl.zycienakodach.pragmaticflights.sdk.application.EventStreamName.streamId;

public class EventStreamNameTestFixtures {

  private static final String TEST_TENANT = "TestTenant";

  public static EventStreamName testTenantEventStream(String category, String id){
    return new EventStreamName(category(category), streamId(id)).withCategoryPrefix(TEST_TENANT);
  }
}

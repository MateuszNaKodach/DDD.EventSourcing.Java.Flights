package pl.zycienakodach.esflights.sdk.application;

import pl.zycienakodach.esflights.sdk.application.eventstream.EventStreamName;

public class EventStreamNameTestFixtures {

  private static final String TEST_TENANT = "TestTenant";

  public static EventStreamName testTenantEventStream(String category, String id) {
    return EventStreamName.ofCategory(category).withId(id).withCategoryPrefix(TEST_TENANT);
  }
}

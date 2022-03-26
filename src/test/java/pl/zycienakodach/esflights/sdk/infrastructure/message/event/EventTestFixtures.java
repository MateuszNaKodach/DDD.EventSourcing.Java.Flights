package pl.zycienakodach.esflights.sdk.infrastructure.message.event;

import pl.zycienakodach.esflights.sdk.application.message.CausationId;
import pl.zycienakodach.esflights.sdk.application.message.CorrelationId;
import pl.zycienakodach.esflights.sdk.application.message.event.EventEnvelope;
import pl.zycienakodach.esflights.sdk.application.message.event.EventId;
import pl.zycienakodach.esflights.sdk.application.message.event.EventMetadata;
import pl.zycienakodach.esflights.sdk.application.tenant.TenantId;

import java.time.Instant;
import java.util.UUID;

public class EventTestFixtures {

  public static SampleEvent anEvent() {
    return new SampleEvent("Sample", 123);
  }

  public static EventEnvelope envelope(Object event) {
    var metadata = new EventMetadata(
        new EventId(UUID.randomUUID().toString()),
        Instant.now(),
        new TenantId("sampleTenantId"),
        new CorrelationId(UUID.randomUUID().toString()),
        new CausationId(UUID.randomUUID().toString())
    );
    return new EventEnvelope(event, metadata);
  }
}

package pl.zycienakodach.esflights.sdk.application.message.event

import pl.zycienakodach.esflights.sdk.application.message.CausationId
import pl.zycienakodach.esflights.sdk.application.message.CorrelationId
import pl.zycienakodach.esflights.sdk.application.tenant.TenantId
import spock.lang.Specification

import java.time.Instant

class EventMetadataSpec extends Specification {

    def "event metadata should store properties"() {
        given:
        def eventId = new EventId("eventId")
        def timestamp = Instant.now()
        def tenantId = new TenantId("tenantId")
        def correlationId = new CorrelationId("correlationId")
        def causationId = new CausationId("causationId")

        when:
        def metadata = new EventMetadata(
                eventId,
                timestamp,
                tenantId,
                correlationId,
                causationId
        )

        then:
        metadata.eventId() == eventId
        metadata.timestamp() == timestamp
        metadata.tenantId() == tenantId
        metadata.correlationId() == correlationId
        metadata.causationId() == causationId
    }

    def "when causationId not passed, then causationId raw value should equal correlationId"() {
        given:
        def eventId = new EventId("eventId")
        def timestamp = Instant.now()
        def tenantId = new TenantId("tenantId")
        def correlationId = new CorrelationId("correlationId")

        when:
        def metadata = new EventMetadata(
                eventId,
                timestamp,
                tenantId,
                correlationId
        )

        then:
        metadata.eventId() == eventId
        metadata.timestamp() == timestamp
        metadata.tenantId() == tenantId
        metadata.correlationId() == correlationId
        metadata.causationId().raw() == correlationId.raw()
    }


    def "map keys should return all metadata"() {
        given:
        def eventId = new EventId("eventId")
        def timestamp = Instant.now()
        def tenantId = new TenantId("tenantId")
        def correlationId = new CorrelationId("correlationId")
        def causationId = new CausationId("causationId")

        when:
        def metadata = new EventMetadata(
                eventId,
                timestamp,
                tenantId,
                correlationId,
                causationId
        )
        metadata.put("AnotherKey", "AnotherValue")

        then:
        metadata == [TenantId: 'tenantId', CausationId: 'causationId', AnotherKey: 'AnotherValue', EventId: 'eventId', CorrelationId: 'correlationId', Timestamp: timestamp.toString()]
    }
}

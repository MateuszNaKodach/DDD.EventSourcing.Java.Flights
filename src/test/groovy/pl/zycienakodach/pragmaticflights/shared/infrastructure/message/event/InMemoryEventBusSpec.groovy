package pl.zycienakodach.pragmaticflights.shared.infrastructure.message.event

import pl.zycienakodach.pragmaticflights.shared.application.message.CorrelationId
import pl.zycienakodach.pragmaticflights.shared.application.message.event.EventId
import pl.zycienakodach.pragmaticflights.shared.application.message.event.EventMetadata
import pl.zycienakodach.pragmaticflights.shared.application.tenant.TenantId
import spock.lang.Specification

import pl.zycienakodach.pragmaticflights.shared.application.message.event.EventHandler

import java.time.Instant;

class InMemoryEventBusSpec extends Specification {

    def "execute registered handlers"() {
        given:
        def eventBus = new InMemoryEventBus()
        def eventHandlerMock1 = Mock(EventHandler<SampleEvent>)
        def eventHandlerMock2 = Mock(EventHandler<SampleEvent>)
        eventBus.subscribe(SampleEvent.class, eventHandlerMock1)
        eventBus.subscribe(SampleEvent.class, eventHandlerMock2)
        def event = new SampleEvent("Sample", 123)
        def metadata = anEventMetadata();

        when:
        eventBus.publish(event, metadata)
        //eventBus.publishAll(List.of(event,event))

        then:
        1 * eventHandlerMock1.accept(event, metadata)
    }

    def "handlers only for another event"() {
        given:
        def eventBus = new InMemoryEventBus()
        def eventHandlerMock1 = Mock(EventHandler<AnotherSampleEvent>)
        def eventHandlerMock2 = Mock(EventHandler<AnotherSampleEvent>)
        eventBus.subscribe(AnotherSampleEvent.class, eventHandlerMock1)
        eventBus.subscribe(AnotherSampleEvent.class, eventHandlerMock2)
        def event = new SampleEvent("Sample", 123)
        def metadata = anEventMetadata();

        when:
        eventBus.publish(event, metadata)
        //eventBus.publishAll(List.of(event,event))

        then:
        0 * eventHandlerMock1.accept(event)
    }

    def "no handlers"() {
        given:
        def eventBus = new InMemoryEventBus()
        def event = new SampleEvent("Sample", 123)
        def metadata = anEventMetadata();

        when:
        eventBus.publish(event, metadata)
        //eventBus.publishAll(List.of(event,event))

        then:
        noExceptionThrown()
    }

    private static EventMetadata anEventMetadata() {
        def generateId = () -> UUID.randomUUID().toString();
        return new EventMetadata(
                new EventId(generateId()),
                Instant.now(),
                new TenantId(generateId()),
                new CorrelationId(generateId())
        )
    }
}

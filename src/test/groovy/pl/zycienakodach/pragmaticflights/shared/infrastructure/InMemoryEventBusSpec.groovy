package pl.zycienakodach.pragmaticflights.shared.infrastructure

import spock.lang.Specification

import java.util.function.Consumer

class InMemoryEventBusSpec extends Specification {

    def "execute registered handlers"() {
        given:
        def eventBus = new InMemoryEventBus()
        def eventHandlerMock1 = Mock(Consumer<SampleEvent>)
        def eventHandlerMock2 = Mock(Consumer<SampleEvent>)
        eventBus.subscribe(SampleEvent.class, eventHandlerMock1)
        eventBus.subscribe(SampleEvent.class, eventHandlerMock2)
        def event = new SampleEvent("Sample", 123)

        when:
        eventBus.publish(event)
        eventBus.publishAll(List.of(event,event))

        then:
        3 * eventHandlerMock1.accept(event)
    }

    def "handlers only for another event"() {
        given:
        def eventBus = new InMemoryEventBus()
        def eventHandlerMock1 = Mock(Consumer<AnotherSampleEvent>)
        def eventHandlerMock2 = Mock(Consumer<AnotherSampleEvent>)
        eventBus.subscribe(AnotherSampleEvent.class, eventHandlerMock1)
        eventBus.subscribe(AnotherSampleEvent.class, eventHandlerMock2)
        def event = new SampleEvent("Sample", 123)

        when:
        eventBus.publish(event)
        eventBus.publishAll(List.of(event,event))

        then:
        0 * eventHandlerMock1.accept(event)
    }

    def "no handlers"() {
        given:
        def eventBus = new InMemoryEventBus()
        def event = new SampleEvent("Sample", 123)

        when:
        eventBus.publish(event)
        eventBus.publishAll(List.of(event,event))

        then:
        noExceptionThrown()
    }
}

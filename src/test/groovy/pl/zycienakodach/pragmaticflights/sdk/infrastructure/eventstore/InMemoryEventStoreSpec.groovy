package pl.zycienakodach.pragmaticflights.sdk.infrastructure.eventstore

import pl.zycienakodach.pragmaticflights.sdk.application.eventstream.EventStreamName
import pl.zycienakodach.pragmaticflights.sdk.application.eventstore.ExpectedStreamVersion
import pl.zycienakodach.pragmaticflights.sdk.application.message.event.EventBus
import spock.lang.Specification

import static pl.zycienakodach.pragmaticflights.sdk.infrastructure.message.event.EventTestFixtures.anEvent
import static pl.zycienakodach.pragmaticflights.sdk.infrastructure.message.event.EventTestFixtures.envelope

class InMemoryEventStoreSpec extends Specification {

    def "writing first event to event store and reading event stream"() {
        given:
        def eventStore = inMemoryEventStore()
        def eventStreamName = EventStreamName.ofCategory("streamCategory").withId("streamId")
        def envelope = envelope(anEvent())

        when:
        eventStore.write(eventStreamName, List.of(envelope), new ExpectedStreamVersion.NotExists())

        then:
        def readEventStream = eventStore.read(eventStreamName)
        readEventStream.version() == 1
        readEventStream.events() == List.of(envelope.event())
    }

    def "writing many events to event store and reading event stream"() {
        given:
        def eventStore = inMemoryEventStore()
        def eventStreamName = EventStreamName.ofCategory("streamCategory").withId("streamId")
        def envelope1 = envelope(anEvent())
        def envelope2 = envelope(anEvent())
        def envelope3 = envelope(anEvent())

        when:
        eventStore.write(eventStreamName, List.of(envelope1, envelope2), new ExpectedStreamVersion.NotExists())
        eventStore.write(eventStreamName, List.of(envelope3), new ExpectedStreamVersion.Exactly(2))

        then:
        def readEventStream = eventStore.read(eventStreamName)
        readEventStream.version() == 3
        readEventStream.events() == List.of(envelope1.event(), envelope2.event(), envelope3.event())
    }


    def "writing events when event stream expected version is different"() {
        given:
        def eventStore = inMemoryEventStore()
        def eventStreamName = EventStreamName.ofCategory("streamCategory").withId("streamId")
        def envelope1 = envelope(anEvent())
        def envelope2 = envelope(anEvent())
        def envelope3 = envelope(anEvent())
        eventStore.write(eventStreamName, List.of(envelope1, envelope2), new ExpectedStreamVersion.NotExists())

        when:
        eventStore.write(eventStreamName, List.of(envelope3), new ExpectedStreamVersion.Exactly(1))

        then:
        def thrownException = thrown(IllegalStateException)
        thrownException.message == 'Expected event stream version is Exactly 1 but current is 2'
    }


    private InMemoryEventStore inMemoryEventStore() {
        new InMemoryEventStore(Stub(EventBus))
    }
}

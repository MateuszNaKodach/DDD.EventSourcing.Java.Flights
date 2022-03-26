package pl.zycienakodach.esflights.sdk.infrastructure.service


import pl.zycienakodach.esflights.sdk.application.eventstream.EventStreamName
import pl.zycienakodach.esflights.sdk.application.idgenerator.IdGenerator
import pl.zycienakodach.esflights.sdk.application.message.command.CommandResult
import pl.zycienakodach.esflights.sdk.application.tenant.TenantId
import pl.zycienakodach.esflights.sdk.domain.DomainLogic
import pl.zycienakodach.esflights.sdk.infrastructure.message.event.SampleEvent
import spock.lang.Specification

import static pl.zycienakodach.esflights.sdk.application.time.TimeProviderFixtures.anyTime
import static pl.zycienakodach.esflights.sdk.infrastructure.eventstore.EventStoreTestFixtures.inMemoryEventStore
import static pl.zycienakodach.esflights.sdk.infrastructure.message.command.CommandTestFixtures.aCommandMetadata

class EventStoreApplicationServiceSpec extends Specification {

    def 'when command accepted, then write events to event stream for specific tenant'() {
        given:
        final timeProvider = anyTime()
        final idGenerator = sequentialIdGenerator()
        final eventStore = inMemoryEventStore()
        final applicationService = new EventStoreApplicationService(eventStore, idGenerator, timeProvider)

        and:
        final eventStreamName = EventStreamName.ofCategory("eventStreamCategory").withId("eventStreamId")
        final tenantId = new TenantId("TestTenant")
        final metadata = aCommandMetadata(tenantId)

        and:
        final domainLogicResultEvents = [new SampleEvent("a", 1), new SampleEvent("b", 2)]
        DomainLogic<SampleEvent> domainLogic = {  domainLogicResultEvents }

        when:
        def result = applicationService.execute(eventStreamName, domainLogic, metadata)

        then:
        result instanceof CommandResult.Accepted

        and:
        def eventStream = eventStore.read(eventStreamName.withCategoryPrefix(tenantId.raw()))
        eventStream.version() == 2
        eventStream.events() == domainLogicResultEvents
    }

    static IdGenerator sequentialIdGenerator() {
        def generatedIds = 0
        return {
            generatedIds += 1
            "generatedId" + generatedIds
        }
    }

}

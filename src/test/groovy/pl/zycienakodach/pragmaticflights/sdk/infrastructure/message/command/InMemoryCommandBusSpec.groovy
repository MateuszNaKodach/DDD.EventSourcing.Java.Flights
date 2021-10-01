package pl.zycienakodach.pragmaticflights.sdk.infrastructure.message.command

import pl.zycienakodach.pragmaticflights.sdk.application.message.command.CommandHandler
import pl.zycienakodach.pragmaticflights.sdk.application.message.command.CommandResult
import spock.lang.Specification

import static pl.zycienakodach.pragmaticflights.sdk.infrastructure.message.command.CommandTestFixtures.aCommandMetadata

class InMemoryCommandBusSpec extends Specification {

    def "execute registered handler"() {
        given:
        def commandBus = new InMemoryCommandBus()
        def commandHandlerMock = Mock(CommandHandler<SampleCommand>)
        commandBus.registerHandler(SampleCommand.class, commandHandlerMock)
        def command = new SampleCommand("Sample", 123)
        def metadata = aCommandMetadata()

        when:
        commandBus.execute(command, metadata)

        then:
        1 * commandHandlerMock.apply(command, metadata)
    }

    def "no handler"() {
        given:
        def commandBus = new InMemoryCommandBus()
        def command = new SampleCommand("Sample", 123)
        def metadata = aCommandMetadata()

        when:
        var result = commandBus.execute(command, metadata)

        then:
        result instanceof CommandResult.Rejected
        result.failureReason().get() == 'Missing handler for SampleCommand'
    }

    def "only one handler for one command type"() {
        given:
        def commandBus = new InMemoryCommandBus()
        def commandHandlerStub1 = Stub(CommandHandler<SampleCommand>)
        commandBus.registerHandler(SampleCommand.class, commandHandlerStub1)

        when:
        def commandHandlerStub2 = Stub(CommandHandler<SampleCommand>)
        commandBus.registerHandler(SampleCommand.class, commandHandlerStub2)

        then:
        def thrownException = thrown(RuntimeException)
        thrownException.message == 'Multiple handlers not allowed for SampleCommand'
    }
}

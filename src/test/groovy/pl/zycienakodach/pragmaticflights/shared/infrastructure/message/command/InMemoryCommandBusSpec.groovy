package pl.zycienakodach.pragmaticflights.shared.infrastructure.message.command

import pl.zycienakodach.pragmaticflights.shared.application.message.command.CommandId
import pl.zycienakodach.pragmaticflights.shared.application.message.command.CommandMetadata
import pl.zycienakodach.pragmaticflights.shared.application.tenant.TenantId
import spock.lang.Specification

import pl.zycienakodach.pragmaticflights.shared.application.message.command.CommandHandler

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
        commandBus.execute(command, metadata)

        then:
        def thrownException = thrown(RuntimeException)
        thrownException.message == 'Missing handler for SampleCommand'
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

    private static CommandMetadata aCommandMetadata() {
        def generateId = () -> UUID.randomUUID().toString();
        return new CommandMetadata(
                new CommandId(generateId()),
                new TenantId(generateId())
        )
    }
}

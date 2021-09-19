package pl.zycienakodach.pragmaticflights.shared.infrastructure

import spock.lang.Specification

import java.util.function.Consumer

class InMemoryCommandBusSpec extends Specification {

    def "execute registered handler"() {
        given:
        def commandBus = new InMemoryCommandBus()
        def commandHandlerMock = Mock(Consumer<SampleCommand>)
        commandBus.register(SampleCommand.class, commandHandlerMock)
        def command = new SampleCommand("Sample", 123)

        when:
        commandBus.handle(command)

        then:
        1 * commandHandlerMock.accept(command)
    }

    def "no handler"() {
        given:
        def commandBus = new InMemoryCommandBus()
        def command = new SampleCommand("Sample", 123)

        when:
        commandBus.handle(command)

        then:
        def thrownException = thrown(RuntimeException)
        thrownException.message == 'Missing handler for SampleCommand'
    }

    def "only one handler for one command type"() {
        given:
        def commandBus = new InMemoryCommandBus()
        def commandHandlerStub1 = Stub(Consumer<SampleCommand>)
        commandBus.register(SampleCommand.class, commandHandlerStub1)

        when:
        def commandHandlerStub2 = Stub(Consumer<SampleCommand>)
        commandBus.register(SampleCommand.class, commandHandlerStub2)

        then:
        def thrownException = thrown(RuntimeException)
        thrownException.message == 'Multiple handlers not allowed for SampleCommand'
    }
}

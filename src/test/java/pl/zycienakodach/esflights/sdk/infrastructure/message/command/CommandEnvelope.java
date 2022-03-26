package pl.zycienakodach.esflights.sdk.infrastructure.message.command;

import pl.zycienakodach.esflights.sdk.application.message.command.CommandMetadata;

public record CommandEnvelope(Object command, CommandMetadata metadata) {
}

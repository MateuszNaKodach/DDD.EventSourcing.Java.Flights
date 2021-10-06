package pl.zycienakodach.pragmaticflights.sdk.infrastructure.message.command;

import pl.zycienakodach.pragmaticflights.sdk.application.message.command.CommandMetadata;

public record CommandEnvelope(Object command, CommandMetadata metadata) {
}

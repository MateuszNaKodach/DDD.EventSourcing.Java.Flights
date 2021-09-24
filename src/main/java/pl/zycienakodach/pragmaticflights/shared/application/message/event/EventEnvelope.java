package pl.zycienakodach.pragmaticflights.shared.application.message.event;

public record EventEnvelope(Object event, EventMetadata metadata) {
}

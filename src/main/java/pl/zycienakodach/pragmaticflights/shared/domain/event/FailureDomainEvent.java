package pl.zycienakodach.pragmaticflights.shared.domain.event;

public interface FailureDomainEvent extends DomainEvent {
  String reason();
}

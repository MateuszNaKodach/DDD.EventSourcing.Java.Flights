package pl.zycienakodach.pragmaticflights.shared.domain;

public interface FailureDomainEvent extends DomainEvent {
  String reason();
}

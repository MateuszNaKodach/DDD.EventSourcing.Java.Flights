package pl.zycienakodach.pragmaticflights.shared.domain;

import io.vavr.control.Either;
import pl.zycienakodach.pragmaticflights.shared.domain.FailureDomainEvent;
import pl.zycienakodach.pragmaticflights.shared.domain.SuccessDomainEvent;

import java.util.List;
import java.util.function.Function;

public interface DomainLogic<EventType> extends Function<List<EventType>, Either<List<FailureDomainEvent>, List<SuccessDomainEvent>>> {
}

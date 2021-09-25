package pl.zycienakodach.pragmaticflights.sdk.domain;

import java.util.List;
import java.util.function.Function;

public interface DomainLogic<EventType> extends Function<List<EventType>, List<EventType>> {
}

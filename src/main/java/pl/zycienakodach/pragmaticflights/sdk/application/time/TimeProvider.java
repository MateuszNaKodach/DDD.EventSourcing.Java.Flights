package pl.zycienakodach.pragmaticflights.sdk.application.time;

import java.time.Instant;
import java.util.function.Supplier;

public interface TimeProvider extends Supplier<Instant> {
}

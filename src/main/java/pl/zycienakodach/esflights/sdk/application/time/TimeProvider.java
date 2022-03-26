package pl.zycienakodach.esflights.sdk.application.time;

import java.time.Instant;
import java.util.function.Supplier;

public interface TimeProvider extends Supplier<Instant> {
}

package pl.zycienakodach.pragmaticflights.sdk.domain;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class CustomCollectors {

  // todo: try to use!
  public static <A, B> Collector<A, ?, B> foldLeft(final B init, final BiFunction<? super B, ? super A, ? extends B> f) {
    return Collectors.collectingAndThen(
        Collectors.reducing(Function.<B>identity(), a -> b -> f.apply(b, a), Function::andThen),
        endo -> endo.apply(init)
    );
  }
}

package winter.utils;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by ybamelcash on 6/26/2015.
 */
public class StreamUtils {
    public static <A, B> List<B> mapToList(Stream<A> stream, Function<A, B> mapFunc) {
        return stream.map(mapFunc).collect(Collectors.toList());
    }

    public static <A> Optional<A> find(Stream<A> stream, Predicate<A> predicate) {
        return stream.filter(predicate).findFirst();
    }
    
    public static <A> boolean exists(Stream<A> stream, Predicate<A> predicate) {
        return find(stream, predicate).map(x -> true).orElse(false);
    }
    
    public static <A> Stream<A> remove(Stream<A> stream, Predicate<A> predicate) {
        return stream.filter(predicate.negate());
    }
}

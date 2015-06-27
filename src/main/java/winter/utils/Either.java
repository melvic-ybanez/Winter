package winter.utils;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Created by ybamelcash on 6/24/2015.
 */
public class Either<A, B> {
    private Optional<A> leftOpt;
    private Optional<B> rightOpt;
    
    private Either(Optional<A> leftOpt, Optional<B> rightOpt) {
        this.leftOpt = leftOpt;
        this.rightOpt = rightOpt;
    }
    
    public static <A, B> Either<A, B> left(A left) {
        return new Either<A, B>(Optional.of(left), Optional.<B>empty());
    }
    
    public static <A, B> Either<A, B> right(B right) {
        return new Either<A, B>(Optional.empty(), Optional.of(right));
    }
    
    public Optional<A> getLeft() {
        return leftOpt;
    }
    
    public Optional<B> getRight() {
        return rightOpt;
    }
    
    public <C> Either<C, B> mapLeft(Function<A, C> f) {
        if (getRight().isPresent()) return Either.right(getRight().get());
        return Either.left(getLeft().map(f::apply).get());
    }
    
    public <C> Either<A, C> mapRight(Function<B, C> f) {
        if (getLeft().isPresent()) return Either.left(getLeft().get());
        return Either.right(getRight().map(f::apply).get());
    }
    
    public boolean hasLeft() {
        return getLeft().isPresent();
    }
    
    public boolean hasRight() {
        return getRight().isPresent();
    }
}

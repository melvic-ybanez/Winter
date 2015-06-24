package winter.utils;

import java.util.Optional;

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
}

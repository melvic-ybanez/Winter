package winter.models.behaviors;

import java.util.Optional;

/**
 * Created by ybamelcash on 7/17/2015.
 */
public interface MatchingParenIndexBehavior {
    public Optional<Integer> apply(String str, char caretParen, char matchingParen);
}

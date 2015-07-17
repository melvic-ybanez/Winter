package winter.models.behaviors;

import java.util.regex.Matcher;

/**
 * Created by ybamelcash on 7/17/2015.
 */
public interface StyleClassBehavior {
    public String apply(Matcher matcher, int parenIndex1, int parenIndex2);
}

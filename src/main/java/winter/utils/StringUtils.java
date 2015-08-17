package winter.utils;

import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by ybamelcash on 7/1/2015.
 */
public class StringUtils {
    public static String repeat(int n, String str) {
        return Collections.nCopies(n, str).stream().collect(Collectors.joining());
    }
    
    public static Pair<String, String> splitAt(String str, int index) {
        String firstHalf = str.substring(0, index);
        String secondHalf = str.substring(index);
        return Pair.of(firstHalf, secondHalf);
    }

    public static boolean isInteger(String str) {
        if (str.isEmpty()) return false;
        if (str.charAt(0) == '-') {
            if (str.length() == 1) return false;
            str = str.substring(1);
        }
        return !str.chars().filter(x -> !Character.isDigit(x)).findFirst().isPresent();
    }

    public static Optional<Integer> asInteger(String str) {
        if (isInteger(str)) {
            return Optional.of(Integer.parseInt(str));
        } else {
            return Optional.empty();
        }
    }
}

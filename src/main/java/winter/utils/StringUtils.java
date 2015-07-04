package winter.utils;

import java.util.Collections;
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
}

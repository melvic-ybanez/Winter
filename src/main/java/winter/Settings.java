package winter;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * Created by ybamelcash on 6/21/2015.
 */
public class Settings {
    private static Config config = ConfigFactory.load();
    
    public static final List<String> TYPES = Settings.getStringList("keywords.types");
    public static final List<String> OPERATORS = Settings.getStringList("keywords.operators");
    public static final List<String> FUNCTION_NAMES = Settings.getStringList("keywords.function-names");
    public static final List<String> DEFINE_COMMANDS = Settings.getStringList("keywords.define-commands");
    public static final List<String> SPECIAL_KEYWORDS = Settings.getStringList("keywords.special-keywords");
    public static final List<String> QUOTES = Settings.getStringList("keywords.quotes");
    
    public static String getString(String key) {
        return getSetting(config -> config.getString(key));
    }
    
    public static List<String> getStringList(String key) {
        return getSetting(config -> config.getStringList(key));
    }
    
    public static <A> A getSetting(Function<Config, A> f) {
        checkValidConfigs();
        return f.apply(config);
    }
    
    private static void checkValidConfigs() {
        config.checkValid(ConfigFactory.defaultReference(), "keywords");
    }
}

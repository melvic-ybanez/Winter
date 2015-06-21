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
    
    public static final List<String> TYPES = config.getStringList("keywords.types");
    public static final List<String> OPERATORS = config.getStringList("keywords.operators");
    public static final List<String> FUNCTION_NAMES = config.getStringList("keywords.operators");
    public static final List<String> DEFINE_COMMANDS = config.getStringList("keywords.define-commands");
    public static final List<String> SPECIAL_KEYWORDS = config.getStringList("keywords.special-keywords");
    
    public static String getStringSetting(String key) {
        return getSetting(config -> config.getString(key));
    }
    
    public static List<String> getStringListSettings(String key) {
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

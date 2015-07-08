package winter;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import javafx.scene.image.ImageView;
import winter.utils.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Created by ybamelcash on 6/21/2015.
 */
public class Settings {
    private static Optional<Config> configOpt = Optional.empty();
    
    public static final List<String> TYPES = Settings.getStringList("keywords.types");
    public static final List<String> OPERATORS = Settings.getStringList("keywords.operators");
    public static final List<String> FUNCTION_NAMES = Settings.getStringList("keywords.function-names");
    public static final List<String> DEFINE_COMMANDS = Settings.getStringList("keywords.define-commands");
    public static final List<String> SPECIAL_KEYWORDS = Settings.getStringList("keywords.special-keywords");
    public static final List<String> QUOTES = Settings.getStringList("keywords.quotes");
    public static final List<? extends Config> SUPPORTED_FILE_FORMATS = 
            Settings.getConfigList("general-settings.supported-file-formats");
    public static final int DEFAULT_TAB_SIZE = getSetting(config -> config.getInt("general-settings.default-tab-size"));
    
    public static String getString(String key) {
        return getSetting(config -> config.getString(key));
    }
    
    public static List<String> getStringList(String key) {
        return getSetting(config -> config.getStringList(key));
    }
    
    public static List<? extends Config> getConfigList(String key) {
        return getSetting(config -> config.getConfigList(key));
    }
    
    public static <A> A getSetting(Function<Config, A> f) {
        checkValidConfigs(); 
        return f.apply(configOpt.get());
    }
    
    private static void checkValidConfigs() {
        if (!configOpt.isPresent()) {
            Config config = ConfigFactory.load();
            configOpt = Optional.of(config);
            Stream.of("keywords", "general-settings").forEach(key -> {
                config.checkValid(ConfigFactory.defaultReference(), key);
            });
        }
    }

    public static final String TAB_STRING = StringUtils.repeat(Settings.DEFAULT_TAB_SIZE, " ");
}

package winter.models.editors;

import winter.Settings;
import winter.controllers.preferences.GeneralPrefController;
import winter.factories.EditorModelBehaviors;
import winter.models.preferences.GeneralPrefModel;
import winter.utils.Either;
import winter.utils.Pair;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ybamelcash on 6/26/2015.
 */
public class MeruemEditorModel extends EditorModel {
    private static final String SETTINGS_PATH = "language.meruem";
    private static final String KEYWORDS_PATH = SETTINGS_PATH + ".keywords";

    public static final List<String> TYPES = Settings.getStringList(KEYWORDS_PATH + ".types");
    public static final List<String> OPERATORS = Settings.getStringList(KEYWORDS_PATH + ".operators");
    public static final List<String> FUNCTION_NAMES = Settings.getStringList(KEYWORDS_PATH + ".function-names");
    public static final List<String> DEFINE_COMMANDS = Settings.getStringList(KEYWORDS_PATH + ".define-commands");
    public static final List<String> SPECIAL_KEYWORDS = Settings.getStringList(KEYWORDS_PATH + ".special-keywords");
    public static final List<String> QUOTES = Settings.getStringList(KEYWORDS_PATH + ".quotes");

    public static final String TYPE_PATTERN = "\\b(" + String.join("|", TYPES) + ")\\b";
    public static final String OPERATOR_PATTERN = "(" + String.join("|", OPERATORS) + ")";
    public static final String FUNCTION_NAME_PATTERN = "\\b(" + String.join("|", FUNCTION_NAMES) + ")\\b";
    public static final String DEFINE_COMMAND_PATTERN = "\\b(" + String.join("|", DEFINE_COMMANDS) + ")\\b";
    public static final String SPECIAL_KEYWORD_PATTERN = "\\b(" + String.join("|", SPECIAL_KEYWORDS) + ")\\b";
    public static final String PAREN_PATTERN = "\\(|\\)";
    public static final String BRACE_PATTERN = "\\{|\\}";
    public static final String STRING_PATTERN = "\"([^\"\\\\]|\\\\.)*\"";
    public static final String CHAR_PATTERN = "(\\\\.)";
    public static final String COMMENT_PATTERN = "(;.*)+";
    public static final String QUOTE_PATTERN = "(" + String.join("|", QUOTES) + ")";

    public static final Pattern PATTERN = Pattern.compile(
            "(?<TYPE>" + TYPE_PATTERN + ")"
                    + "|(?<OPERATOR>" + OPERATOR_PATTERN + ")"
                    + "|(?<FUNCTION>" + FUNCTION_NAME_PATTERN + ")"
                    + "|(?<DEFINE>" + DEFINE_COMMAND_PATTERN + ")"
                    + "|(?<SPECIAL>" + SPECIAL_KEYWORD_PATTERN + ")"
                    + "|(?<PAREN>" + PAREN_PATTERN + ")"
                    + "|(?<BRACE>" + BRACE_PATTERN + ")"
                    + "|(?<STRING>" + STRING_PATTERN + ")"
                    + "|(?<CHAR>" + CHAR_PATTERN + ")"
                    + "|(?<COMMENT>" + COMMENT_PATTERN + ")"
                    + "|(?<QUOTE>" + QUOTE_PATTERN + ")");

    private GeneralPrefModel generalPrefModel;
    
    public MeruemEditorModel(Either<Integer, Path> pathEither, GeneralPrefModel generalPrefModel) {
        super();
        this.generalPrefModel = generalPrefModel;
        setPathEither(pathEither);
        setActiveParenIndexesBehavior(EditorModelBehaviors.createDefaultActiveParenIndexes());
        setAutoIndentedLineStringBehavior(EditorModelBehaviors.createLispAutoIndentedLineString());
        setParenIndexesBehavior(EditorModelBehaviors.createDefaultParenIndexes());
        setMatchingParenIndexBehavior(EditorModelBehaviors.createDefaultMatchingParenIndex());
        setStyleClassBehavior(EditorModelBehaviors.createMeruemStyleClass());
    }
    
    public Optional<Pair<Integer, Integer>> getParenIndexes(char paren) {
        return getParenIndexesBehavior().apply(this).apply(paren);
    }

    @Override
    public Optional<Integer> getMatchingParenIndex(String str, char caretParen, char matchingParen) {
        return getMatchingParenIndexBehavior().apply(str, caretParen, matchingParen);
    }

    @Override
    public String getAutoIndentedNewLineString() {
        return getAutoIndentedLineStringBehavior().apply(this);
    }

    public Optional<Pair<Integer, Integer>> getActiveParenIndexes() {
        return getActiveParenIndexesBehavior().apply(this);
    }

    @Override
    public String getStyleClass(Matcher matcher, int parenIndex1, int parenIndex2) {
        return getStyleClassBehavior().apply(matcher, parenIndex1, parenIndex2);
    }

    @Override
    public GeneralPrefModel getGeneralPrefModel() {
        return generalPrefModel;
    }
}

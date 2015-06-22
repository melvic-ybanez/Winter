package winter;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.StyleSpans;
import org.fxmisc.richtext.StyleSpansBuilder;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ybamelcash on 6/21/2015.
 */
public class EditorPane extends BorderPane {
    private TabPane tabPane = new TabPane();
    private List<String> openedFiles = new ArrayList<>();
    
    public static final String TYPE_PATTERN = "\\b(" + String.join("|", Settings.TYPES) + ")\\b";
    public static final String OPERATOR_PATTERN = "(" + String.join("|", Settings.OPERATORS) + ")";
    public static final String FUNCTION_NAME_PATTERN = "\\b(" + String.join("|", Settings.FUNCTION_NAMES) + ")\\b";
    public static final String DEFINE_COMMAND_PATTERN = "\\b(" + String.join("|", Settings.DEFINE_COMMANDS) + ")\\b";
    public static final String SPECIAL_KEYWORD_PATTERN = "\\b(" + String.join("|", Settings.SPECIAL_KEYWORDS) + ")\\b";
    public static final String PAREN_PATTERN = "\\(|\\)";
    public static final String BRACE_PATTERN = "\\{|\\}";
    public static final String STRING_PATTERN = "\"([^\"\\\\]|\\\\.)*\"";
    public static final String CHAR_PATTERN = "(\\\\.)";
    public static final String COMMENT_PATTERN = "(;.*)+";
    public static final String QUOTE_PATTERN = "(" + String.join("|", Settings.QUOTES) + ")";
    
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
    
    public EditorPane() {
        setCenter(tabPane);
        createUntitledTab();
    }
    
    public void newEditorAreaTab(String title, String content) {
        if (openedFiles.contains(title)) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("File already exists.");
            alert.setHeaderText("The file " + title + " already exists.");
            alert.setContentText("Do you want to replace it with the new one?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                tabPane.getTabs().removeIf(tab -> tab.getText().equals(title));
                openedFiles.remove(title);
                newEditorAreaTab(title, content);
            } 
        } else {
            Tab tab = new Tab(title);
            CodeArea codeArea = createEditorArea();
            codeArea.replaceText(0, 0, content);
            tab.setContent(codeArea);
            tabPane.getTabs().add(tab);
            tabPane.getSelectionModel().select(tab);
            openedFiles.add(title);
            tab.setOnClosed(event -> {
                openedFiles.remove(tab.getText());
            });
        }
    }

    private void createUntitledTab() {
        newEditorAreaTab("Untitled", "");
    }
    
    private CodeArea createEditorArea() {
        CodeArea editorArea = new CodeArea();
        editorArea.setParagraphGraphicFactory(LineNumberFactory.get(editorArea));
        editorArea.textProperty().addListener((obs, oldText, newText) -> {
            editorArea.setStyleSpans(0, highlight(newText));
        });
        return editorArea;
    }
    
    private StyleSpans<Collection<String>> highlight(String text) {
        Matcher matcher = PATTERN.matcher(text);
        int lastMatched = 0;
        StyleSpansBuilder<Collection<String>> builder = new StyleSpansBuilder<>();
        while (matcher.find()) {
            String styleClass = null;
            if (matcher.group("TYPE") != null) styleClass = "type";
            if (matcher.group("OPERATOR") != null) styleClass = "operator";
            if (matcher.group("FUNCTION") != null) styleClass = "function-name";
            if (matcher.group("DEFINE") != null) styleClass = "define-command";
            if (matcher.group("SPECIAL") != null) styleClass = "special-keyword";
            if (matcher.group("PAREN") != null) styleClass = "paren";
            if (matcher.group("BRACE") != null) styleClass = "brace";
            if (matcher.group("STRING") != null) styleClass = "string";
            if (matcher.group("CHAR") != null) styleClass = "char";
            if (matcher.group("COMMENT") != null) styleClass = "comment";
            if (matcher.group("QUOTE") != null) styleClass = "quote";
            assert styleClass != null;
            builder.add(Collections.emptyList(), matcher.start() - lastMatched);
            builder.add(Collections.singleton(styleClass), matcher.end() - matcher.start());
            lastMatched = matcher.end();
        }
        builder.add(Collections.emptyList(), text.length() - lastMatched);
        return builder.create();
    }
}

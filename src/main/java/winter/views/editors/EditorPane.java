package winter.views.editors;

import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.StyleSpans;
import org.fxmisc.richtext.StyleSpansBuilder;
import winter.controllers.EditorController;
import winter.models.EditorModel;
import winter.utils.Either;
import winter.utils.Pair;
import winter.views.Settings;

import java.nio.file.Path;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ybamelcash on 6/21/2015.
 */
public class EditorPane extends BorderPane {
    private TabPane tabPane = new TabPane();
    private List<EditorModel> editors = new ArrayList<>();
    private int untitledCount = 0;
    
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
        createContextMenu();
        newUntitledTab();
    }
    
    private void createContextMenu() {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem closeItem = new MenuItem("Close");
        MenuItem closeOtherItem = new MenuItem("Close others");
        MenuItem closeAllItem = new MenuItem("Close All");
        
        closeItem.setOnAction(e -> closeCurrentTab());
        closeOtherItem.setOnAction(e -> EditorController.closeOtherTabs());
        closeAllItem.setOnAction(e -> closeAllTabs());
        
        closeItem.setAccelerator(new KeyCodeCombination(KeyCode.W, KeyCombination.CONTROL_DOWN));
        
        contextMenu.getItems().addAll(closeItem, closeOtherItem, closeAllItem);
        tabPane.setContextMenu(contextMenu);
    }
    
    public void newEditorAreaTab(Either<String, Path> pathEither, String contents) {
        EditorModel editorModel = new EditorModel(pathEither);
        Optional<Path> pathOpt = editorModel.getPath();
        String title = editorModel.titleProperty().getValue();
        
        if (pathOpt.isPresent() && EditorController.exists(editors, pathOpt.get())) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("File already exists.");
            alert.setHeaderText("The file " + title + " already exists.");
            alert.setContentText("Do you want to replace it with the new one?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                tabPane.getTabs().removeIf(tab -> tab.getText().equals(title));
                editors = EditorController.remove(editors, pathOpt.get());
                newEditorAreaTab(pathEither, contents);
            } 
        } else {
            Tab tab = new Tab(title);
            CodeArea codeArea = createEditorArea();
            codeArea.replaceText(0, 0, contents); 
            tab.setContent(codeArea); 
            editorModel.contentsProperty().bind(codeArea.textProperty());
            editorModel.caretPositionProperty().bind(codeArea.caretPositionProperty());
            
            tabPane.getTabs().add(tab); 
            tabPane.getSelectionModel().select(tab);
            
            editors.add(editorModel);
            tab.textProperty().bind(editorModel.titleProperty());
            
            tab.setOnClosed(event -> {
                pathOpt.ifPresent(path -> editors = EditorController.remove(editors, path));
                if (editors.isEmpty()) {
                    newUntitledTab();
                }
            });
        }
    }
    
    public void newEditorAreaTab(Path path, String contents) {
        newEditorAreaTab(Either.right(path), contents);
    }

    public void newUntitledTab() {
        String suffix = untitledCount == 0 ? "" : untitledCount + "";
        String title = "Untitled" + suffix;
        newEditorAreaTab(Either.left(title), "");
        untitledCount++;
    }
    
    private CodeArea createEditorArea() {
        CodeArea editorArea = new CodeArea();
        
        // These values are hard-coded for now.
        editorArea.setStyle("-fx-font:13px Consolas");
        
        editorArea.setParagraphGraphicFactory(LineNumberFactory.get(editorArea));
        editorArea.textProperty().addListener((obs, oldText, newText) -> {
            if (!oldText.equals(newText)) {
                editorAreaChanged(editorArea, newText);
            }
        }); 
        editorArea.caretPositionProperty().addListener((obs, oldPos, newPos) -> {
            if (!oldPos.equals(newPos)) {
                editorAreaChanged(editorArea, editorArea.getText());
            }
        });
        editorArea.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            switch (event.getCode()) {
                case ESCAPE: getParent().requestFocus(); break;
                case TAB:
                    editorArea.insertText(editorArea.getCaretPosition(), Settings.TAB_STRING);
                    event.consume();
                    break;
                case ENTER:
                    Pair<String, Integer> result = EditorController.getAutoIndentedContents();
                    editorArea.replaceText(result.getFirst());
                    editorArea.positionCaret(result.getSecond());
                    event.consume();
                    break;

            }
        });
        return editorArea;
    }
    
    private StyleSpans<Collection<String>> highlight(String text, int parenIndex1, int parenIndex2) {
        StyleSpansBuilder<Collection<String>> builder = new StyleSpansBuilder<>();
        int page = 0;
        int pageSize = text.length() > 1000 ? 1000 : text.length();
        String textToMatch = text.substring(page, page + pageSize); 
        
        while (!textToMatch.isEmpty()) {
            Matcher matcher = PATTERN.matcher(textToMatch);
            int lastMatched = 0;
            
            while (matcher.find()) {
                String styleClass = null;
                if (matcher.group("TYPE") != null) styleClass = "type";
                if (matcher.group("OPERATOR") != null) styleClass = "operator";
                if (matcher.group("FUNCTION") != null) styleClass = "function-name";
                if (matcher.group("DEFINE") != null) styleClass = "define-command";
                if (matcher.group("SPECIAL") != null) styleClass = "special-keyword";
                if (matcher.group("PAREN") != null) {
                    if (matcher.start() == parenIndex1 || matcher.start() == parenIndex2) {
                        styleClass = "focused-paren";
                    } 
                } 
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
            builder.add(Collections.emptyList(), textToMatch.length() - lastMatched);
            
            page += pageSize;
            if (page == text.length()) break;
            if (page + pageSize > text.length()) {
                pageSize = text.length() - page;
            }
            textToMatch = text.substring(page, page + pageSize);
        }
        
        return builder.create();
    }
    
    private void editorAreaChanged(CodeArea editorArea, String newText) {
        if (!newText.isEmpty()) {
            Optional<Pair<Integer, Integer>> parenIndexesOpt = EditorController.getActiveParenIndexes();
            int parenIndex1 = -1;
            int parenIndex2 = -1;
            if (parenIndexesOpt.isPresent()) {
                Pair<Integer, Integer> parenIndexes = parenIndexesOpt.get();
                parenIndex1 = parenIndexes.getFirst();
                parenIndex2 = parenIndexes.getSecond();
            }
            editorArea.setStyleSpans(0, highlight(newText, parenIndex1, parenIndex2));
        }
    }
    
    private void closeCurrentTab() {
        Tab selectedTab = getTabPane().getSelectionModel().getSelectedItem();
        EditorController.closeTab(selectedTab);
    }
    
    private void closeAllTabs() {
        EditorController.closeAllTabs();
        newUntitledTab();
    }
    
    public TabPane getTabPane() {
        return tabPane;
    }
    
    public List<EditorModel> getEditors() {
        return editors;
    }
}

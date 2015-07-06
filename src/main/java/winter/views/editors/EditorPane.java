package winter.views.editors;

import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.StyleSpans;
import org.fxmisc.richtext.StyleSpansBuilder;
import winter.controllers.EditorController;
import winter.controllers.FileController;
import winter.models.EditorModel;
import winter.utils.Either;
import winter.utils.Errors;
import winter.utils.Pair;
import winter.views.Settings;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
        tabPane.getStyleClass().add("meruem-tabpane");
    }
    
    private void createContextMenu() {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem renameItem = new MenuItem("Rename");
        MenuItem closeItem = new MenuItem("Close");
        MenuItem closeOtherItem = new MenuItem("Close Others");
        MenuItem closeAllItem = new MenuItem("Close All");
        
        renameItem.setOnAction(e -> renameTab());
        closeItem.setOnAction(e -> closeCurrentTab());
        closeOtherItem.setOnAction(e -> EditorController.closeOtherTabs());
        closeAllItem.setOnAction(e -> closeAllTabs());
        
        closeItem.setAccelerator(new KeyCodeCombination(KeyCode.W, KeyCombination.CONTROL_DOWN));
        
        contextMenu.getItems().addAll(renameItem, closeItem, closeOtherItem, closeAllItem);
        tabPane.setContextMenu(contextMenu);
    }
    
    public void newEditorAreaTab(Either<Integer, Path> pathEither, String contents) {
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
            editors.add(editorModel);
            CodeArea codeArea = createEditorArea();
            codeArea.replaceText(0, 0, contents); 
            
            tab.setContent(codeArea); 
            tab.textProperty().bind(editorModel.titleProperty());
            
            tab.setGraphic(new Label());
            
            editorModel.contentsProperty().bind(codeArea.textProperty());
            editorModel.caretPositionProperty().bind(codeArea.caretPositionProperty());
            editorModel.setOrigContents(contents);
            
            tabPane.getTabs().add(tab); 
            tabPane.getSelectionModel().select(tab);
            
            tab.textProperty().bind(editorModel.titleProperty());
            tab.setOnClosed(event -> {
                editorModel.getPath().ifPresent(path -> editors = EditorController.remove(editors, path));
                if (!editorModel.getPath().isPresent())
                    editors = editors.stream().filter(model -> !model.getTitle().equals(editorModel.getTitle()))
                            .collect(Collectors.toList());
                if (editors.isEmpty()) {
                    newUntitledTab();
                }
            });
        }
    }
    
    public void newEditorAreaTab(Path path, String contents) {
        newEditorAreaTab(Either.right(path), contents);
    }

    public void newUntitledTab(int untitledCount) {
        newEditorAreaTab(Either.left(untitledCount), "");
        this.untitledCount = ++untitledCount;
    }
    
    public void newUntitledTab() {
        newUntitledTab(this.untitledCount);
    }
    
    private CodeArea createEditorArea() {
        CodeArea editorArea = new CodeArea();
        editorArea.getStyleClass().add("meruem-codearea");
        
        /* These are hardcoded values for now */
        editorArea.setStyle("-fx-font-family: Consolas"); 

        editorArea.setParagraphGraphicFactory(LineNumberFactory.get(editorArea));
        editorArea.textProperty().addListener((obs, oldText, newText) -> {
            EditorController.editorAreaChanged(editorArea, newText);
        }); 
        editorArea.caretPositionProperty().addListener((obs, oldPos, newPos) -> {
            EditorController.editorAreaChanged(editorArea, editorArea.getText());
        }); 
        editorArea.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (EditorController.runAccelerators(event))
                return;

            switch (event.getCode()) {
                case TAB:
                    editorArea.insertText(editorArea.getCaretPosition(), Settings.TAB_STRING);
                    event.consume();
                    break;
                case ENTER:
                    String autoIndentedNewLineString = EditorController.getAutoIndentedNewLineString();
                    editorArea.insertText(editorArea.getCaretPosition(), autoIndentedNewLineString);
                    event.consume();
                    break;
            }
        });
        return editorArea;
    }
    
    private void closeCurrentTab() {
        Tab selectedTab = getTabPane().getSelectionModel().getSelectedItem();
        EditorController.closeTab(selectedTab);
        if (tabPane.getTabs().isEmpty()) {
            newUntitledTab();
        }
    }
    
    private void closeAllTabs() {
        EditorController.closeAllTabs();
        newUntitledTab();
    }
    
    private void renameTab() {
        EditorModel activeEditor = EditorController.getActiveEditor();
        activeEditor.ifUntitled(count -> {
            Errors.headerLessDialog(Errors.titles.RENAME, "Can not rename a non-existing file");
        });
        activeEditor.getPath().ifPresent(path -> {
            TextInputDialog renameDialog = new TextInputDialog(activeEditor.getTitle());
            renameDialog.setTitle("Rename File");
            renameDialog.setHeaderText(null);
            renameDialog.setContentText("Enter the new filename");

            Optional<String> newFilenameOpt = renameDialog.showAndWait();
            newFilenameOpt.ifPresent(newFilename -> {
                Either<IOException, Either<String, Path>> result = FileController.renameFile(path, newFilename);
                result.ifLeft(ex -> Errors.exceptionDialog(Errors.titles.RENAME, null, ex.getMessage(), ex));
                result.ifRight(right -> {
                    right.ifLeft(error -> Errors.headerLessDialog(Errors.titles.RENAME, error));
                    right.ifRight(activeEditor::setPath);
                });
            });    
        });
    }
    
    public TabPane getTabPane() {
        return tabPane;
    }
    
    public List<EditorModel> getEditors() {
        return editors;
    }
}

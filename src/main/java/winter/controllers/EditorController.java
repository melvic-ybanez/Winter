package winter.controllers;

import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.StyleSpans;
import org.fxmisc.richtext.StyleSpansBuilder;
import winter.Globals;
import winter.models.EditorModel;
import winter.utils.*;
import winter.views.Settings;
import winter.views.editors.EditorPane;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

/**
 * Created by ybamelcash on 6/26/2015.
 */
public class EditorController {
    public static EditorModel getActiveEditor() {
        EditorPane editorPane = Globals.editorPane;
        int activeIndex = editorPane.getTabPane().getSelectionModel().getSelectedIndex();
        return editorPane.getEditors().get(activeIndex);
    }
    
    public static void renameSelectedTab(Path newPath) {
        EditorModel editorModel = EditorController.getActiveEditor();
        editorModel.setPath(newPath);
    }
    
    public static String getActiveEditorLastLine() {
        return getActiveEditor().getLastLine();
    }
    
    public static Optional<Pair<Integer, Integer>> getActiveParenIndexes() {
        EditorModel activeEditor = getActiveEditor();
        Optional<Pair<Integer, Integer>> parenIndexes = activeEditor.getParenIndexes('(');
        if (parenIndexes.isPresent()) return parenIndexes;
        else return activeEditor.getParenIndexes(')');
    }
    
    public static String getAutoIndentedNewLineString() {
        EditorModel activeEditor = getActiveEditor();
        int caretPos = activeEditor.getCaretPosition();
        Pair<String, String> pair = StringUtils.splitAt(activeEditor.getContents(), caretPos);
        Optional<Integer> openParenIndexOpt = activeEditor.getMatchingParenIndex(
                new StringBuilder(pair.getFirst()).reverse().toString(), ')', '(');
        
        return openParenIndexOpt.map(openParenIndex -> {
            int realOpenParenIndex = pair.getFirst().length() - openParenIndex - 1;
            String stringBeforeOpenParen = StringUtils.splitAt(pair.getFirst(), realOpenParenIndex).getFirst();
            int startCharCount = 0;
            
            for (int i = stringBeforeOpenParen.length() - 1; i > -1; i--) {
                char c = stringBeforeOpenParen.charAt(i);
                if (c == '\n') break;
                startCharCount++;
            }

            return "\n" + Settings.TAB_STRING + StringUtils.repeat(startCharCount, " ");
        }).orElseGet(() -> "\n");
    }

    public static void editorAreaChanged(CodeArea editorArea, String newText) {
        TabPane tabPane = Globals.editorPane.getTabPane();
        if (!newText.isEmpty() && tabPane.getSelectionModel().getSelectedIndex() != -1) {
            Optional<Pair<Integer, Integer>> parenIndexesOpt = EditorController.getActiveParenIndexes();
            int parenIndex1 = -1;
            int parenIndex2 = -1;
            if (parenIndexesOpt.isPresent()) {
                Pair<Integer, Integer> parenIndexes = parenIndexesOpt.get();
                parenIndex1 = parenIndexes.getFirst();
                parenIndex2 = parenIndexes.getSecond();
            }
            editorArea.setStyleSpans(0, getStyleSpans(newText, parenIndex1, parenIndex2));
        }
        
        updateTabGraphic();
    }
    
    public static void updateTabGraphic() {
        if (Globals.editorPane.getTabPane().getSelectionModel().getSelectedIndex() != -1) {
            Label graphicLabel = (Label) Globals.editorPane.getTabPane()
                    .getSelectionModel().getSelectedItem().getGraphic();
            if (getActiveEditor().unsaved()) {
                graphicLabel.setText("*");
            } else graphicLabel.setText("");
        }
    }

    public static StyleSpans<Collection<String>> getStyleSpans(String text, int parenIndex1, int parenIndex2) {
        StyleSpansBuilder<Collection<String>> builder = new StyleSpansBuilder<>();
        int page = 0;
        int pageSize = text.length() > 1000 ? 1000 : text.length();
        String textToMatch = text.substring(page, page + pageSize);

        while (!textToMatch.isEmpty()) {
            Matcher matcher = EditorPane.PATTERN.matcher(textToMatch);
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
    
    public static CodeArea getActiveCodeArea() {
        return (CodeArea) Globals.editorPane.getTabPane().getSelectionModel().getSelectedItem().getContent();
    }
    
    public static void undo() {
        getActiveCodeArea().undo();
    }
    
    public static void redo() {
        getActiveCodeArea().redo();
    }
    
    public static void copy() {
        getActiveCodeArea().copy();
    }
    
    public static void cut() {
        getActiveCodeArea().cut();
    }
    
    public static void paste() {
        getActiveCodeArea().paste();
    }
    
    public static boolean runAccelerators(KeyEvent event) {
        if (!event.getCode().isModifierKey()) {
            Function<KeyCombination.Modifier[], Boolean> runAccelerator = (modifiers) -> {
                Runnable r = Globals.editorPane.getScene().getAccelerators()
                        .get(new KeyCodeCombination(event.getCode(), modifiers));
                if (r != null) {
                    r.run();
                    return true;
                }
                return false;
            };

            List<KeyCombination.Modifier> modifiers = new ArrayList<>();
            if (event.isControlDown()) modifiers.add(KeyCodeCombination.CONTROL_DOWN);
            if (event.isShiftDown()) modifiers.add(KeyCodeCombination.SHIFT_DOWN);
            if (event.isAltDown()) modifiers.add(KeyCodeCombination.ALT_DOWN);

            return runAccelerator.apply(modifiers.toArray(new KeyCombination.Modifier[modifiers.size()]));
        }
        return false;
    }
    
    public static void openFile() {
        FileChooser openFileChooser = Globals.menus.fileMenu.getOpenFileChooser();
        Globals.menus.fileMenu.showOpenDialog().ifPresent(file -> {
            Path path = file.toPath();
            Either<IOException, String> result = FileController.openFile(path);
            result.getLeft().ifPresent(Errors::openFileException);
            result.getRight().ifPresent(contents -> {
                Globals.editorPane.newEditorAreaTab(path, contents);
            });
            openFileChooser.setInitialDirectory(file.getParentFile());
        });
    }
    
    public static void saveFile(EditorModel editorModel) {
        Either<IOException, Boolean> result = FileController.saveFile(editorModel.getPath(), editorModel.getContents());
        result.ifLeft(Errors::saveFileException);
        result.ifRight(saved -> {
            if (saved) {
                editorModel.save();
            } else {
                saveAsFile(editorModel);
            } 
        });
    }
    
    public static void saveAsFile(EditorModel editorModel) {
        FileChooser saveFileChooser = Globals.menus.fileMenu.getSaveFileChooser();
        Globals.menus.fileMenu.showSaveDialog().ifPresent(file -> {
            Path path = file.toPath();
            Optional<IOException> errorOpt = FileController.saveAsFile(path, editorModel.getContents());

            errorOpt.ifPresent(Errors::saveFileException);
            if (!errorOpt.isPresent()) {
                saveFileChooser.setInitialDirectory(file.getParentFile());
                EditorController.renameSelectedTab(path);
                editorModel.save();
            }
        });
    }

    public static boolean whenHasChanges(EditorModel editorModel, Runnable function) {
        if (editorModel.unsaved()) {
            Alert saveAlert = new Alert(Alert.AlertType.CONFIRMATION); 
            saveAlert.setHeaderText(null);
            saveAlert.setContentText("Do you want to save " + editorModel.getTitle() + "?");
            saveAlert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
            Optional<ButtonType> buttonType = saveAlert.showAndWait();
            return buttonType.map(button -> {
                if (button == ButtonType.YES) {
                    saveFile(editorModel);
                } else if (button == ButtonType.CANCEL) {
                    return false;
                }
                function.run();
                return true;
            }).orElse(false);
        } else {
            function.run();
            return true;
        }
    }
    
    public static boolean closeTab(Tab tab) {
        int index = Globals.editorPane.getTabPane().getTabs().indexOf(tab);
        EditorModel editorModel = Globals.editorPane.getEditors().get(index);
        
        boolean toClose = whenHasChanges(editorModel, () -> {
            EditorPane editorPane = Globals.editorPane;
            editorPane.getTabPane().getTabs().remove(tab);
            final List<EditorModel> editors = Globals.editorPane.getEditors();
            editorModel.getPath().ifPresent(path -> {
                Globals.editorPane.setEditors(EditorController.remove(editors, path));
            });
            if (!editorModel.getPath().isPresent())
                Globals.editorPane.setEditors(editors.stream().filter(model -> !model.getTitle().equals(editorModel.getTitle()))
                        .collect(Collectors.toList()));
        });

        if (Globals.editorPane.getEditors().isEmpty()) {
            Globals.editorPane.newUntitledTab();
        }
        
        return toClose;
    }
    
    public static void closeOtherTabs() {
        EditorModel editorModel = getActiveEditor();
        
        int selectedIndex = Globals.editorPane.getTabPane().getSelectionModel().getSelectedIndex();
        Globals.editorPane.getTabPane().getTabs().remove(selectedIndex);
        Globals.editorPane.getEditors().remove(selectedIndex);
        
        closeAllTabs();
        Globals.editorPane.newEditorAreaTab(editorModel);
    }
    
    public static void closeAllTabs() {
        List<EditorModel> unclosedEditors = new ArrayList<>();
        ObservableList<Tab> tabs = Globals.editorPane.getTabPane().getTabs();
        for (int i = 0; i < tabs.size(); i++) {
            Tab tab = tabs.get(i);
            EditorModel editorModel = Globals.editorPane.getEditors().get(i);
            if (!whenHasChanges(editorModel, () -> {})) {
                unclosedEditors.add(editorModel);
            }
        }
        if (unclosedEditors.size() != tabs.size()) {
            Globals.editorPane.getTabPane().getTabs().clear();
            Globals.editorPane.getEditors().clear();
            unclosedEditors.forEach(Globals.editorPane::newEditorAreaTab);
        }
    }
    
    public static Optional<EditorModel> find(List<EditorModel> editors, Path path) {
        return StreamUtils.find(editors.stream(), model -> model.equalsPath(path));
    }
    
    public static boolean exists(List<EditorModel> editors, Path path) {
        return StreamUtils.exists(editors.stream(), model -> model.equalsPath(path));
    }
    
    public static List<EditorModel> remove(List<EditorModel> editors, Path path) {
        return StreamUtils.remove(editors.stream(), model -> model.equalsPath(path)).collect(Collectors.toList());
    }
}

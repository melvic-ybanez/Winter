package winter.controllers;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Tab;
import javafx.stage.FileChooser;
import org.fxmisc.richtext.CodeArea;
import winter.Globals;
import winter.models.EditorModel;
import winter.utils.*;
import winter.views.Settings;
import winter.views.editors.EditorPane;
import winter.views.menus.FileMenu;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
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
    
    public static CodeArea getActiveCodeArea() {
        return (CodeArea) Globals.editorPane.getTabPane().getSelectionModel().getSelectedItem().getContent();
    }
    
    public static void undo() {
        getActiveCodeArea().undo();
    }
    
    public static void redo() {
        getActiveCodeArea().redo();
    }
    
    public static void openFile() {
        FileChooser openFileChooser = Globals.menus.fileMenu.getOpenFileChooser();

        Optional.ofNullable(openFileChooser.showOpenDialog(Globals.getMainStage())).ifPresent(file -> {
            Path path = file.toPath();
            Either<IOException, String> result = FileController.openFile(path);
            result.getLeft().ifPresent(Errors::openFileException);
            result.getRight().ifPresent(contents -> {
                Globals.editorPane.newEditorAreaTab(path, contents);
            });
            openFileChooser.setInitialDirectory(file.getParentFile());
        });
    }
    
    public static void saveFile() {
        Either<IOException, Boolean> result = FileController.saveFile();
        result.ifLeft(Errors::saveFileException);
        result.ifRight(saved -> {
            if (!saved) {
                saveAsFile();
            }
        });
    }
    
    public static void saveAsFile() {
        FileChooser saveFileChooser = Globals.menus.fileMenu.getSaveFileChooser();
        Optional.ofNullable(saveFileChooser.showSaveDialog(Globals.getMainStage())).ifPresent(file -> {
            Path path = file.toPath();
            Optional<IOException> errorOpt = FileController.saveAsFile(path);

            errorOpt.ifPresent(Errors::saveFileException);
            if (!errorOpt.isPresent()) {
                saveFileChooser.setInitialDirectory(file.getParentFile());
                EditorController.renameSelectedTab(path);
            }
        });
    }
    
    public static void closeTab(Tab tab) {
        EditorPane editorPane = Globals.editorPane;
        EventHandler<Event> handler = tab.getOnClosed();
        editorPane.getTabPane().getTabs().remove(tab);
        handler.handle(null);
    }
    
    public static void closeOtherTabs() {
        EditorModel editorModel = getActiveEditor();
        closeAllTabs();
        editorModel.getPath().ifPresent(path -> {
            Globals.editorPane.newEditorAreaTab(path, editorModel.getContents());
        });
        editorModel.ifUntitled(Globals.editorPane::newUntitledTab);
    }
    
    public static void closeAllTabs() {
        Globals.editorPane.getTabPane().getTabs().clear();
        Globals.editorPane.getEditors().clear();
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

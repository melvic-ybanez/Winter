package winter.controllers;

import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import org.fxmisc.richtext.CodeArea;
import winter.Application;
import winter.models.MeruemEditorModel;
import winter.utils.*;
import winter.views.editors.EditorPane;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by ybamelcash on 6/26/2015.
 */
public class EditorsControllerImpl implements EditorsController {
    public static MeruemEditorModel getActiveEditor() {
        EditorPane editorPane = Application.editorPane;
        int activeIndex = editorPane.getTabPane().getSelectionModel().getSelectedIndex();
        return editorPane.getEditors().get(activeIndex);
    }
    
    public static void renameSelectedTab(Path newPath) {
        MeruemEditorModel editorModel = EditorsControllerImpl.getActiveEditor();
        editorModel.setPath(newPath);
    }
    
    public static String getActiveEditorLastLine() {
        return getActiveEditor().getLastLine();
    }

    public static void editorAreaChanged(CodeArea editorArea, String newText) {
        TabPane tabPane = Application.editorPane.getTabPane();
        if (!newText.isEmpty() && tabPane.getSelectionModel().getSelectedIndex() != -1) {
            Optional<Pair<Integer, Integer>> parenIndexesOpt = EditorsControllerImpl.getActiveParenIndexes();
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
        if (Application.editorPane.getTabPane().getSelectionModel().getSelectedIndex() != -1) {
            Label graphicLabel = (Label) Application.editorPane.getTabPane()
                    .getSelectionModel().getSelectedItem().getGraphic();
            if (getActiveEditor().unsaved()) {
                graphicLabel.setText("*");
            } else graphicLabel.setText("");
        }
    }
    
    public static CodeArea getActiveCodeArea() {
        return (CodeArea) Application.editorPane.getTabPane().getSelectionModel().getSelectedItem().getContent();
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
                Runnable r = Application.editorPane.getScene().getAccelerators()
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
        FileChooser openFileChooser = Application.menus.fileMenu.getOpenFileChooser();
        Application.menus.fileMenu.showOpenDialog().ifPresent(file -> {
            Path path = file.toPath();
            Either<IOException, String> result = FileController.openFile(path);
            result.getLeft().ifPresent(Errors::openFileException);
            result.getRight().ifPresent(contents -> {
                Application.editorPane.newEditorAreaTab(path, contents);
            });
            openFileChooser.setInitialDirectory(file.getParentFile());
        });
    }
    
    public static void saveFile(MeruemEditorModel editorModel) {
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
    
    public static void saveAsFile(MeruemEditorModel editorModel) {
        FileChooser saveFileChooser = Application.menus.fileMenu.getSaveFileChooser();
        Application.menus.fileMenu.showSaveDialog().ifPresent(file -> {
            Path path = file.toPath();
            Optional<IOException> errorOpt = FileController.saveAsFile(path, editorModel.getContents());

            errorOpt.ifPresent(Errors::saveFileException);
            if (!errorOpt.isPresent()) {
                saveFileChooser.setInitialDirectory(file.getParentFile());
                EditorsControllerImpl.renameSelectedTab(path);
                editorModel.save();
            }
        });
    }

    public static boolean whenHasChanges(MeruemEditorModel editorModel, Runnable function) {
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
        int index = Application.editorPane.getTabPane().getTabs().indexOf(tab);
        MeruemEditorModel editorModel = Application.editorPane.getEditors().get(index);
        
        boolean toClose = whenHasChanges(editorModel, () -> {
            EditorPane editorPane = Application.editorPane;
            editorPane.getTabPane().getTabs().remove(tab);
            final List<MeruemEditorModel> editors = Application.editorPane.getEditors();
            editorModel.getPath().ifPresent(path -> {
                Application.editorPane.setEditors(EditorsControllerImpl.remove(editors, path));
            });
            if (!editorModel.getPath().isPresent())
                Application.editorPane.setEditors(editors.stream().filter(model -> !model.getTitle().equals(editorModel.getTitle()))
                        .collect(Collectors.toList()));
        });

        if (Application.editorPane.getEditors().isEmpty()) {
            Application.editorPane.newUntitledTab();
        }
        
        return toClose;
    }
    
    public static void closeOtherTabs() {
        MeruemEditorModel editorModel = getActiveEditor();
        
        int selectedIndex = Application.editorPane.getTabPane().getSelectionModel().getSelectedIndex();
        Application.editorPane.getTabPane().getTabs().remove(selectedIndex);
        Application.editorPane.getEditors().remove(selectedIndex);
        
        closeAllTabs();
        Application.editorPane.newEditorAreaTab(editorModel);
    }
    
    public static boolean closeAllTabs() {
        List<MeruemEditorModel> unclosedEditors = new ArrayList<>();
        ObservableList<Tab> tabs = Application.editorPane.getTabPane().getTabs();
        for (int i = 0; i < tabs.size(); i++) {
            Tab tab = tabs.get(i);
            MeruemEditorModel editorModel = Application.editorPane.getEditors().get(i);
            if (!whenHasChanges(editorModel, () -> {})) {
                unclosedEditors.add(editorModel);
            }
        }
        if (unclosedEditors.size() != tabs.size()) {
            Application.editorPane.getTabPane().getTabs().clear();
            Application.editorPane.getEditors().clear();
            unclosedEditors.forEach(Application.editorPane::newEditorAreaTab);
        }
        return unclosedEditors.isEmpty();
    }
    
    public static Optional<MeruemEditorModel> find(List<MeruemEditorModel> editors, Path path) {
        return StreamUtils.find(editors.stream(), model -> model.equalsPath(path));
    }
    
    public static boolean exists(List<MeruemEditorModel> editors, Path path) {
        return StreamUtils.exists(editors.stream(), model -> model.equalsPath(path));
    }
    
    public static List<MeruemEditorModel> remove(List<MeruemEditorModel> editors, Path path) {
        return StreamUtils.remove(editors.stream(), model -> model.equalsPath(path)).collect(Collectors.toList());
    }
}

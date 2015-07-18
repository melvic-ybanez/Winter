package winter.controllers;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import winter.Application;
import winter.models.EditorModel;
import winter.utils.Either;
import winter.utils.Errors;
import winter.utils.FileUtils;
import winter.utils.Pair;
import winter.views.editors.EditorView;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * Created by ybamelcash on 7/18/2015.
 */
public class EditorControllerImpl implements EditorController {
    private EditorModel editorModel;
    private EditorView editorView;
    
    public EditorControllerImpl(EditorModel editorModel) {
        this.editorModel = editorModel;
        editorView = new EditorView(this, editorModel);
    }

    @Override
    public void rename(Path newPath) {
        editorModel.setPath(newPath);
    }

    @Override
    public void editorAreaChanged(String newText) {
        if (!newText.isEmpty()) {
            Optional<Pair<Integer, Integer>> parenIndexesOpt = editorModel.getActiveParenIndexes();
            int parenIndex1 = -1;
            int parenIndex2 = -1;
            if (parenIndexesOpt.isPresent()) {
                Pair<Integer, Integer> parenIndexes = parenIndexesOpt.get();
                parenIndex1 = parenIndexes.getFirst();
                parenIndex2 = parenIndexes.getSecond();
            }
            editorView.setStyleSpans(0, editorModel.getStyleSpans(newText, parenIndex1, parenIndex2));
        }

        updateTabGraphic();
    }

    @Override
    public void updateTabGraphic() {
        if (editorModel.unsaved()) {
            editorView.graphicProperty().setValue(new Label("*"));
        } else editorView.graphicProperty().setValue(new Label(""));
    }

    @Override
    public boolean runAccelerators(KeyEvent event) {
        if (!event.getCode().isModifierKey()) {
            Function<KeyCombination.Modifier[], Boolean> runAccelerator = (modifiers) -> {
                Runnable r = editorView.getScene().getAccelerators()
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

    @Override
    public boolean whenHasChanges(Runnable function) {
        if (editorModel.unsaved()) {
            Alert saveAlert = new Alert(Alert.AlertType.CONFIRMATION);
            saveAlert.setHeaderText(null);
            saveAlert.setContentText("Do you want to save " + editorModel.getTitle() + "?");
            saveAlert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
            Optional<ButtonType> buttonType = saveAlert.showAndWait();
            return buttonType.map(button -> {
                if (button == ButtonType.YES) {
                    saveFile();
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

    @Override
    public void autoIndent() {
        String autoIndentedNewLineString = editorModel.getAutoIndentedNewLineString();
        editorView.insertText(editorView.getCaretPosition(), autoIndentedNewLineString);
    }

    public void saveFile() {
        Either<IOException, Boolean> result = FileUtils.saveFile(editorModel.getPath(), editorModel.getContents());
        result.ifLeft(Errors::saveFileException);
        result.ifRight(saved -> {
            if (saved) {
                editorModel.save();
            } else {
                saveAsFile();
            }
        });
    }

    public void saveAsFile() {
        FileChooser saveFileChooser = Application.menus.fileMenu.getSaveFileChooser();
        Application.menus.fileMenu.showSaveDialog().ifPresent(file -> {
            Path path = file.toPath();
            Optional<IOException> errorOpt = FileUtils.saveAsFile(path, editorModel.getContents());

            errorOpt.ifPresent(Errors::saveFileException);
            if (!errorOpt.isPresent()) {
                saveFileChooser.setInitialDirectory(file.getParentFile());
                editorModel.setPath(path);
                editorModel.save();
            }
        });
    }

    @Override
    public void undo() {
        editorView.undo();
    }

    @Override
    public void redo() {
        editorView.redo();
    }

    @Override
    public void copy() {
        editorView.copy();
    }

    @Override
    public void cut() {
        editorView.cut();
    }

    @Override
    public void paste() {
        editorView.paste();
    }

    public EditorView getEditorView() {
        return editorView;
    }

    @Override
    public EditorModel getEditorModel() {
        return editorModel;
    }
}

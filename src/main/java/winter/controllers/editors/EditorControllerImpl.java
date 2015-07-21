package winter.controllers.editors;

import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import winter.controllers.files.FileController;
import winter.models.editors.EditorModel;
import winter.utils.Either;
import winter.utils.Errors;
import winter.utils.FileUtils;
import winter.utils.Pair;
import winter.views.editor.EditorView;

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
    private FileController fileController;
    private EditorSetController editorSetController;
    
    public EditorControllerImpl(EditorModel editorModel, EditorSetController editorSetController) {
        this.editorModel = editorModel;
        this.editorSetController = editorSetController;
        editorView = new EditorView(this, editorModel);
    }

    @Override
    public void rename() {
        editorModel.ifUntitled(count -> {
            Errors.headerLessDialog(Errors.titles.RENAME, "Can not rename a non-existing file");
        });
        editorModel.getPath().ifPresent(path -> {
            Optional<String> newFilenameOpt = editorView.showRenameDialog();
            newFilenameOpt.ifPresent(newFilename -> {
                Either<IOException, Either<String, Path>> result = FileUtils.renameFile(path, newFilename);
                result.ifLeft(ex -> Errors.exceptionDialog(Errors.titles.RENAME, null, ex.getMessage(), ex));
                result.ifRight(right -> {
                    right.ifLeft(error -> Errors.headerLessDialog(Errors.titles.RENAME, error));
                    right.ifRight(editorModel::setPath);
                });
            });
        });
    }

    @Override
    public void editorAreaChanged(String newText) {
        if (!newText.isEmpty()) {
            int selectedIndex = editorSetController.getEditorSetView().getTabPane()
                    .getSelectionModel().getSelectedIndex();
            if (selectedIndex != -1) {
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
            Optional<ButtonType> buttonType = editorView.showUnsavedDialog();
            return buttonType.map(button -> {
                if (button == ButtonType.YES) {
                    fileController.saveFile();
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

    public FileController getFileController() {
        return fileController;
    }

    public void setFileController(FileController fileController) {
        this.fileController = fileController;
    }
}

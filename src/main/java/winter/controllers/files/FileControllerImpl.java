package winter.controllers.files;

import javafx.stage.FileChooser;
import winter.controllers.editors.EditorSetController;
import winter.controllers.projects.ProjectSetController;
import winter.models.editors.EditorModel;
import winter.utils.Either;
import winter.utils.Errors;
import winter.utils.FileUtils;
import winter.views.editor.EditorSetView;
import winter.views.menus.FileMenu;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

/**
 * Created by ybamelcash on 7/19/2015.
 */
public class FileControllerImpl implements FileController {
    FileMenu fileMenu;
    
    public FileControllerImpl(EditorSetController editorSetController, ProjectSetController projectSetController) {
        setFileMenu(new FileMenu(this, editorSetController, projectSetController));
    }
    
    @Override
    public void openFile() {
        FileChooser openFileChooser = fileMenu.getOpenFileChooser();
        fileMenu.showOpenDialog().ifPresent(file -> {
            Path path = file.toPath();
            Either<IOException, String> result = FileUtils.openFile(path);
            result.getLeft().ifPresent(Errors::openFileExceptionDialog);
            result.getRight().ifPresent(contents -> {
                EditorSetView editorSetView = fileMenu.getEditorSetController().getEditorSetView();
                editorSetView.newEditorAreaTab(path, contents);
            });
            openFileChooser.setInitialDirectory(file.getParentFile());
        });
    }

    @Override
    public boolean saveFile() {
        return saveFile(getEditorSetController().getActiveEditorModel());
    }

    @Override
    public boolean saveFileAs() {
        return saveFileAs(getEditorSetController().getActiveEditorModel());
    }

    @Override
    public boolean saveFile(EditorModel editorModel) {
        Either<IOException, Boolean> result = FileUtils.saveFile(editorModel.getPath(), editorModel.getContents());
        if (result.hasLeft()) {
            IOException error = result.getLeft().get();
            Errors.saveFileExceptionDialog(error);
            return false;
        }
        return result.getRight().map(saved -> {
            if (saved) {
                editorModel.save();
                return true;
            } else {
                return saveFileAs(editorModel);
            }
        }).get();
    }

    @Override
    public boolean saveFileAs(EditorModel editorModel) {
        FileChooser saveFileChooser = fileMenu.getSaveFileChooser();
        Optional<File> result = fileMenu.showSaveDialog();
        return result.map(file -> {
            Path path = file.toPath();
            Optional<IOException> errorOpt = FileUtils.saveAsFile(path, editorModel.getContents());

            errorOpt.ifPresent(Errors::saveFileExceptionDialog);
            if (!errorOpt.isPresent()) {
                saveFileChooser.setInitialDirectory(file.getParentFile());
                editorModel.setPath(path);
                editorModel.save();
            }
            return !errorOpt.isPresent();
        }).orElseGet(() -> false);
    }

    public FileMenu getFileMenu() {
        return fileMenu;
    }

    public void setFileMenu(FileMenu fileMenu) {
        this.fileMenu = fileMenu;
    }
    
    public EditorSetController getEditorSetController() {
        return fileMenu.getEditorSetController();
    }
}
